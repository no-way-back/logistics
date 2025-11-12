package com.nowayback.user.presentation;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.nowayback.common.exception.GlobalExceptionHandler;
import com.nowayback.common.security.config.AuthUserArgumentResolver;
import com.nowayback.common.security.config.CommonWebConfig;
import com.nowayback.common.security.interceptor.RoleCheckInterceptor;
import com.nowayback.user.application.UserService;
import com.nowayback.user.application.dto.result.DeleteUserResult;
import com.nowayback.user.application.exception.UserApplicationErrorCode;
import com.nowayback.user.application.exception.UserApplicationException;
import com.nowayback.user.infrastructure.security.JwtTokenProvider;

@WebMvcTest(UserController.class)
@Import({
	AuthUserArgumentResolver.class,
	RoleCheckInterceptor.class,
	CommonWebConfig.class,
	GlobalExceptionHandler.class
})
class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private UserService userService;

	@MockitoBean
	private JwtTokenProvider jwtTokenProvider;

	@Nested
	@DisplayName("유저 삭제")
	class DeleteUser {

		@Test
		@DisplayName("DELETE /users/{userId} 성공")
		void deleteUser_Success() throws Exception {
			// given
			UUID userId = UUID.randomUUID();
			UUID deletedBy = UUID.randomUUID();

			DeleteUserResult result = new DeleteUserResult(userId);

			given(userService.deleteUser(any(UUID.class), any(UUID.class)))
				.willReturn(result);

			// when & then
			mockMvc.perform(delete("/users/{userId}", userId)
					.header("X-User-Id", deletedBy.toString())
					.header("X-User-Role", "MASTER")
					.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.userId").value(userId.toString()))
				.andExpect(jsonPath("$.message").value("사용자가 삭제되었습니다."));

			then(userService).should(times(1)).deleteUser(userId, deletedBy);
		}

		@Test
		@DisplayName("DELETE /users/{userId} - 존재하지 않는 사용자 (404)")
		void deleteUser_WhenUserNotFount_ThrowException() throws Exception {
			// given
			UUID userId = UUID.randomUUID();
			UUID deletedBy = UUID.randomUUID();

			given(userService.deleteUser(any(UUID.class), any(UUID.class)))
				.willThrow(new UserApplicationException(UserApplicationErrorCode.USER_NOT_FOUND));

			// when & then
			mockMvc.perform(delete("/users/{userId}", userId)
					.header("X-User-Id", deletedBy.toString())
					.header("X-User-Role", "MASTER")
					.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.code").value("USER_2001"))
				.andExpect(jsonPath("$.message").value("사용자를 찾을 수 없습니다"));
		}

		@Test
		@DisplayName("DELETE /users/{userId} - 이미 삭제된 사용자 (400)")
		void deleteUser_WhenAlreadyDeleted_ThrowException() throws Exception {
			// given
			UUID userId = UUID.randomUUID();
			UUID deletedBy = UUID.randomUUID();

			given(userService.deleteUser(any(UUID.class), any(UUID.class)))
				.willThrow(new UserApplicationException(UserApplicationErrorCode.USER_ALREADY_DELETED));

			// when & then
			mockMvc.perform(delete("/users/{userId}", userId)
					.header("X-User-Id", deletedBy.toString())
					.header("X-User-Role", "MASTER")
					.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.code").value("USER_2005"))
				.andExpect(jsonPath("$.message").value("이미 삭제된 사용자입니다"));
		}

		@ParameterizedTest
		@ValueSource(strings = {"HUB_MANAGER", "DELIVERY_MANAGER", "COMPANY_MANAGER"})
		@DisplayName("DELETE /users/{userId} - MASTER가 아닌 역할은 접근 불가 (403)")
		void deleteUser_WhenNonMasterRole_ThrowException(String role) throws Exception {
			// given
			UUID userId = UUID.randomUUID();
			UUID deletedBy = UUID.randomUUID();

			// when & then
			mockMvc.perform(delete("/users/{userId}", userId)
					.header("X-User-Id", deletedBy.toString())
					.header("X-User-Role", role)
					.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isForbidden());

			then(userService).should(never()).deleteUser(any(), any());
		}

		@Test
		@DisplayName("DELETE /users/{userId} - 인증 헤더 없음 (401)")
		void deleteUser_WhenNoAuthHeader_ThrowException() throws Exception {
			// given
			UUID userId = UUID.randomUUID();

			// when & then
			mockMvc.perform(delete("/users/{userId}", userId)
					.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isUnauthorized());

			then(userService).should(never()).deleteUser(any(), any());
		}
	}
}
