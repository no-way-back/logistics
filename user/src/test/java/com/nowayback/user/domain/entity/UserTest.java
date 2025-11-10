package com.nowayback.user.domain.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import com.nowayback.common.security.annotation.UserRole;
import com.nowayback.user.domain.exception.UserDomainException;

class UserTest {

	@Test
	@DisplayName("사용자 생성 시 초기 상태는 PENDING이다")
	void createUser_ShouldSetInitialStatusPending() {
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

	@Test
	@DisplayName("PENDING 상태의 사용자를 승인하면 APPROVED로 변경된다")
	void approveSignup_WhenPending_ShouldChangeStatusToApproved() {
		// given
		User user = User.createUser("user1", "password", UserRole.HUB_MANAGER, null);

		// when
		user.approveSignup();

		// then
		assertThat(user.getStatus()).isEqualTo(UserStatus.APPROVED);
	}

	@Test
	@DisplayName("PENDING이 아닌 상태에서 승인 시도하면 예외 발생")
	void approveSignup_WhenNotPending_ShouldThrowException() {
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
	void rejectSignup_WhenPending_ShouldChangeStatusToRejected() {
		// given
		User user = User.createUser("user1", "password", UserRole.DELIVERY_MANAGER, null);

		// when
		user.rejectSignup();

		// then
		assertThat(user.getStatus()).isEqualTo(UserStatus.REJECTED);
	}

	@Test
	@DisplayName("PENDING이 아닌 상태에서 거절 시도하면 예외 발생")
	void rejectSignup_WhenNotPending_ShouldThrowException() {
		// given
		User user = User.createUser("user1", "password", UserRole.HUB_MANAGER, null);
		user.rejectSignup();

		// when & then
		assertThatThrownBy(user::rejectSignup)
			.isInstanceOf(UserDomainException.class)
			.hasMessageContaining("이미 처리된 요청입니다");
	}

	@Test
	@DisplayName("APPROVED 상태의 사용자는 로그인할 수 있다")
	void validateCanLogin_WhenApproved_ShouldNotThrowException() {
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
	void validateCanLogin_WhenNotApproved_ShouldThrowException(UserStatus status) {
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

	@ParameterizedTest
	@EnumSource(value = UserStatus.class, names = {"APPROVED", "REJECTED"})
	@DisplayName("승인 상태 검증 - APPROVED 또는 REJECTED만 유효")
	void validateApprovalStatus_WithValidStatus_ShouldNotThrowException(UserStatus status) {
		// given
		User user = User.createUser("user1", "password", UserRole.MASTER, null);

		// when & then
		assertThatCode(() -> user.validateApprovalStatus(status))
			.doesNotThrowAnyException();
	}

	@Test
	@DisplayName("승인 상태 검증 - PENDING은 유효하지 않음")
	void validateApprovalStatus_WithPending_ShouldThrowException() {
		// given
		User user = User.createUser("user1", "password", UserRole.MASTER, null);

		// when & then
		assertThatThrownBy(() -> user.validateApprovalStatus(UserStatus.PENDING))
			.hasMessageContaining("유효하지 않은 승인 상태입니다");
	}
}