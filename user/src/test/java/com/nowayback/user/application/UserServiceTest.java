package com.nowayback.user.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.nowayback.common.security.annotation.UserRole;
import com.nowayback.user.application.dto.result.DeleteUserResult;
import com.nowayback.user.application.exception.UserApplicationErrorCode;
import com.nowayback.user.application.exception.UserApplicationException;
import com.nowayback.user.domain.entity.User;
import com.nowayback.user.domain.exception.UserDomainException;
import com.nowayback.user.domain.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserService userService;

	private UUID targetUserId;
	private UUID deletedBy;
	private User targetUser;

	@BeforeEach
	void setUp() {
		targetUserId = UUID.randomUUID();
		deletedBy = UUID.randomUUID();
		targetUser = User.createUser("testuser", "password", UserRole.COMPANY_MANAGER, null);
		ReflectionTestUtils.setField(targetUser, "userId", targetUserId);
	}

	@Nested
	@DisplayName("유저 삭제")
	class DeleteUser {

		@Test
		@DisplayName("사용자 삭제 성공")
		void deleteUser_Success() {
			// given
			given(userRepository.findByUserIdAndDeletedAtIsNull(targetUserId))
				.willReturn(Optional.of(targetUser));
			given(userRepository.save(any(User.class)))
				.willReturn(targetUser);

			// when
			DeleteUserResult result = userService.deleteUser(targetUserId, deletedBy);

			// then
			assertThat(result).isNotNull();
			assertThat(result.userId()).isEqualTo(targetUser.getUserId());

			then(userRepository).should(times(1)).findByUserIdAndDeletedAtIsNull(targetUserId);
			then(userRepository).should(times(1)).save(any(User.class));
		}

		@Test
		@DisplayName("존재하지 않는 사용자 삭제 시도 시 예외 발생")
		void deleteUser_NotFound_ThrowException() {
			// given
			given(userRepository.findByUserIdAndDeletedAtIsNull(targetUserId))
				.willReturn(Optional.empty());

			// when & then
			assertThatThrownBy(() -> userService.deleteUser(targetUserId, deletedBy))
				.isInstanceOf(UserApplicationException.class)
				.hasFieldOrPropertyWithValue("errorCode", UserApplicationErrorCode.USER_NOT_FOUND);

			then(userRepository).should(never()).save(any());
		}

		@Test
		@DisplayName("이미 삭제된 사용자 삭제 시도 시 예외 발생")
		void deleteUser_AlreadyDeleted_ThrowException() {
			// given
			User deletedUser = User.createUser("deleted", "password", UserRole.HUB_MANAGER, null);
			deletedUser.delete(UUID.randomUUID());

			given(userRepository.findByUserIdAndDeletedAtIsNull(targetUserId))
				.willReturn(Optional.of(deletedUser));

			// when & then
			assertThatThrownBy(() -> userService.deleteUser(targetUserId, deletedBy))
				.isInstanceOf(UserDomainException.class)
				.hasMessageContaining("이미 삭제된 사용자입니다.");

			then(userRepository).should(never()).save(any());
		}
	}

}
