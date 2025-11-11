package com.nowayback.user.domain.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import com.nowayback.common.security.annotation.UserRole;
import com.nowayback.user.application.exception.UserApplicationErrorCode;
import com.nowayback.user.application.exception.UserApplicationException;
import com.nowayback.user.domain.exception.UserDomainException;

class UserTest {

	@Nested
	@DisplayName("유저 생성")
	class CreateUser {

		@Test
		@DisplayName("사용자 생성 시 초기 상태는 PENDING이다")
		void createUser_SetInitialStatusPending() {
			// given
			String username = "testuser";
			String password = "encodedPassword";
			UserRole role = UserRole.COMPANY_MANAGER;
			String slackId = "U12345";

			// when
			User user = User.createUser(username, password, role, slackId);

			// then
			assertThat(user.getUsername()).isEqualTo(username);
			assertThat(user.getPassword()).isEqualTo(password);
			assertThat(user.getRole()).isEqualTo(role);
			assertThat(user.getSlackId()).isEqualTo(slackId);
			assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING);
		}
	}

	@Nested
	@DisplayName("유저 승인")
	class ApproveUser {

		@Test
		@DisplayName("PENDING 상태의 사용자를 승인하면 APPROVED로 변경된다")
		void approveSignup_WhenPending_ChangeStatusToApproved() {
			// given
			User user = User.createUser("user1", "password", UserRole.HUB_MANAGER, null);

			// when
			user.approveSignup();

			// then
			assertThat(user.getStatus()).isEqualTo(UserStatus.APPROVED);
		}

		@Test
		@DisplayName("PENDING이 아닌 상태에서 승인 시도하면 예외 발생")
		void approveSignup_WhenNotPending_ThrowException() {
			// given
			User user = User.createUser("user1", "password", UserRole.HUB_MANAGER, null);
			user.approveSignup();

			// when & then
			assertThatThrownBy(user::approveSignup)
				.isInstanceOf(UserDomainException.class)
				.hasMessageContaining("이미 처리된 요청입니다");
		}

		@Test
		@DisplayName("PENDING 상태의 사용자를 거절하면 REJECTED로 변경된다")
		void rejectSignup_WhenPending_ChangeStatusToRejected() {
			// given
			User user = User.createUser("user1", "password", UserRole.DELIVERY_MANAGER, null);

			// when
			user.rejectSignup();

			// then
			assertThat(user.getStatus()).isEqualTo(UserStatus.REJECTED);
		}

		@Test
		@DisplayName("PENDING이 아닌 상태에서 거절 시도하면 예외 발생")
		void rejectSignup_WhenNotPending_ThrowException() {
			// given
			User user = User.createUser("user1", "password", UserRole.HUB_MANAGER, null);
			user.rejectSignup();

			// when & then
			assertThatThrownBy(user::rejectSignup)
				.isInstanceOf(UserDomainException.class)
				.hasMessageContaining("이미 처리된 요청입니다");
		}

		@ParameterizedTest
		@EnumSource(value = UserStatus.class, names = {"APPROVED", "REJECTED"})
		@DisplayName("승인 상태 검증 - APPROVED 또는 REJECTED만 유효")
		void validateApprovalStatus_WithValidStatus_Success(UserStatus status) {
			// given
			User user = User.createUser("user1", "password", UserRole.MASTER, null);

			// when & then
			assertThatCode(() -> user.validateApprovalStatus(status))
				.doesNotThrowAnyException();
		}

		@Test
		@DisplayName("승인 상태 검증 - PENDING은 유효하지 않음")
		void validateApprovalStatus_WithPending_ThrowException() {
			// given
			User user = User.createUser("user1", "password", UserRole.MASTER, null);

			// when & then
			assertThatThrownBy(() -> user.validateApprovalStatus(UserStatus.PENDING))
				.hasMessageContaining("유효하지 않은 승인 상태입니다");

		}
	}


	@Nested
	@DisplayName("로그인")
	class LoginUser {

		@Test
		@DisplayName("APPROVED 상태의 사용자는 로그인할 수 있다")
		void validateCanLogin_WhenApproved_Success() {
			// given
			User user = User.createUser("user1", "password", UserRole.MASTER, null);
			user.approveSignup();

			// when & then
			assertThatCode(user::validateCanLogin)
				.doesNotThrowAnyException();
		}

		@ParameterizedTest
		@EnumSource(value = UserStatus.class, names = {"PENDING", "REJECTED"})
		@DisplayName("APPROVED가 아닌 상태에서는 로그인할 수 없다")
		void validateCanLogin_WhenNotApproved_ThrowException(UserStatus status) {
			// given
			User user = User.createUser("user1", "password", UserRole.MASTER, null);
			if (status == UserStatus.REJECTED) {
				user.rejectSignup();
			}

			// when & then
			assertThatThrownBy(user::validateCanLogin)
				.isInstanceOf(UserDomainException.class)
				.hasMessageContaining("승인되지 않은 사용자입니다");
		}
	}

	@Nested
	@DisplayName("사용자 삭제")
	class DeleteUser {

		@Test
		@DisplayName("사용자 삭제 시 deleted_at과 deleted_by가 설정된다")
		void deleteUser_ShouldSetDeletedFields() {
			// given
			User user = User.createUser("testuser", "password", UserRole.COMPANY_MANAGER, null);
			UUID deletedBy = UUID.randomUUID();

			// when
			user.delete(deletedBy);

			// then
			assertThat(user.getDeletedAt()).isNotNull();
			assertThat(user.getDeletedBy()).isEqualTo(deletedBy);
		}

		@Test
		@DisplayName("이미 삭제된 사용자는 다시 삭제할 수 없다")
		void deleteUser_AlreadyDeleted_ShouldThrowException() {
			// given
			User user = User.createUser("testuser", "password", UserRole.HUB_MANAGER, null);
			UUID deletedBy = UUID.randomUUID();
			user.delete(deletedBy);

			// when & then
			assertThatThrownBy(() -> user.delete(deletedBy))
				.isInstanceOf(UserApplicationException.class)
				.hasFieldOrPropertyWithValue("errorCode", UserApplicationErrorCode.USER_ALREADY_DELETED);
		}

		@ParameterizedTest
		@EnumSource(UserRole.class)
		@DisplayName("모든 역할의 사용자가 삭제 가능하다")
		void deleteUser_AllRoles_ShouldBeDeleteable(UserRole role) {
			// given
			User user = User.createUser("testuser", "password", role, null);
			UUID deletedBy = UUID.randomUUID();

			// when
			user.delete(deletedBy);

			// then
			assertThat(user.getDeletedAt()).isNotNull();
			assertThat(user.getDeletedBy()).isEqualTo(deletedBy);
		}

		@Test
		@DisplayName("삭제된 사용자는 삭제 전 정보를 유지한다")
		void deleteUser_ShouldKeepOriginalData() {
			// given
			String originalUsername = "testuser";
			UserRole originalRole = UserRole.DELIVERY_MANAGER;
			User user = User.createUser(originalUsername, "password", originalRole, "U12345");
			user.approveSignup(); // APPROVED 상태로 변경

			UUID deletedBy = UUID.randomUUID();

			// when
			user.delete(deletedBy);

			// then
			assertThat(user.getUsername()).isEqualTo(originalUsername);
			assertThat(user.getRole()).isEqualTo(originalRole);
			assertThat(user.getStatus()).isEqualTo(UserStatus.APPROVED); // 상태도 유지
			assertThat(user.getSlackId()).isEqualTo("U12345");
		}

		@Test
		@DisplayName("삭제는 사용자 상태와 무관하게 수행된다")
		void deleteUser_WorksForAnyStatus() {
			// given: PENDING 상태
			User pendingUser = User.createUser("pending", "password", UserRole.MASTER, null);

			// given: REJECTED 상태
			User rejectedUser = User.createUser("rejected", "password", UserRole.HUB_MANAGER, null);
			rejectedUser.rejectSignup();

			// given: APPROVED 상태
			User approvedUser = User.createUser("approved", "password", UserRole.COMPANY_MANAGER, null);
			approvedUser.approveSignup();

			UUID deletedBy = UUID.randomUUID();

			// when & then
			assertThatCode(() -> pendingUser.delete(deletedBy)).doesNotThrowAnyException();
			assertThatCode(() -> rejectedUser.delete(deletedBy)).doesNotThrowAnyException();
			assertThatCode(() -> approvedUser.delete(deletedBy)).doesNotThrowAnyException();
		}
	}
}