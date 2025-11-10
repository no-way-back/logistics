package com.nowayback.user.presentation;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nowayback.common.dto.PageResponse;
import com.nowayback.user.application.UserService;
import com.nowayback.user.application.dto.command.ApprovalCommand;
import com.nowayback.user.application.dto.command.LoginUserCommand;
import com.nowayback.user.application.dto.command.SignupUserCommand;
import com.nowayback.user.application.dto.command.UpdateUserCommand;
import com.nowayback.user.application.dto.result.LoginResult;
import com.nowayback.user.application.dto.result.UserResult;
import com.nowayback.user.domain.entity.UserStatus;
import com.nowayback.user.infrastructure.security.JwtTokenProvider;
import com.nowayback.user.presentation.dto.request.ApprovalRequest;
import com.nowayback.user.presentation.dto.request.LoginUserRequest;
import com.nowayback.user.presentation.dto.request.SignupUserRequest;
import com.nowayback.user.presentation.dto.request.UpdateUserRequest;
import com.nowayback.user.presentation.dto.response.LoginResponse;
import com.nowayback.user.presentation.dto.response.UpdateUserResponse;
import com.nowayback.user.presentation.dto.response.UserResponse;

import jakarta.validation.Valid;
import com.nowayback.common.security.annotation.AuthUser;
import com.nowayback.common.security.annotation.CurrentUser;
import com.nowayback.common.security.annotation.RequireRole;
import com.nowayback.common.security.annotation.UserRole;

@RestController
@RequestMapping("/users")
public class UserController {

	private final UserService userService;
	private final JwtTokenProvider jwtTokenProvider;

	public UserController(UserService userService, JwtTokenProvider jwtTokenProvider) {
		this.userService = userService;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@PostMapping("/signup")
	public ResponseEntity<UserResponse> signup(
		@Valid @RequestBody SignupUserRequest request
	) {
		SignupUserCommand command = new SignupUserCommand(
			request.username(),
			request.password(),
			request.role(),
			request.slackId()
		);

		UserResult result = userService.signup(command);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(UserResponse.withMessage(result, "회원가입 요청이 완료되었습니다. 관리자 승인을 기다려주세요."));
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(
		@Valid @RequestBody LoginUserRequest request
	) {
		LoginUserCommand command = new LoginUserCommand(
			request.username(),
			request.password()
		);

		LoginResult result = userService.login(command);

		String token = jwtTokenProvider.createToken(
			result.userId(),
			result.username(),
			result.role()
		);

		return ResponseEntity.ok(LoginResponse.of(token));
	}

	@PatchMapping("/{userId}/approval")
	@RequireRole(UserRole.MASTER)
	public ResponseEntity<UserResponse> approveSignup(
		@PathVariable UUID userId,
		@Valid @RequestBody ApprovalRequest request,
		@CurrentUser AuthUser authUser
	) {
		ApprovalCommand command = new ApprovalCommand(
			userId,
			request.status(),
			request.reason(),
			authUser.userId()
		);

		UserResult result = userService.approveOrRejectSignup(command);

		String message = request.status() == UserStatus.APPROVED
			? "회원가입이 승인되었습니다."
			: "회원가입이 거절되었습니다.";

		return ResponseEntity.ok(UserResponse.withMessage(result, message));
	}

	@GetMapping("/me")
	public ResponseEntity<UserResponse> getMyInfo(
		@CurrentUser AuthUser authUser
	) {
		UserResult result = userService.getMyInfo(authUser.userId());
		return ResponseEntity.ok(UserResponse.from(result));
	}

	@GetMapping
	@RequireRole(UserRole.MASTER)
	public ResponseEntity<PageResponse<UserResponse>> getUserList(
		@CurrentUser AuthUser authUser,
		@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
		Pageable pageable
	) {
		PageResponse<UserResult> results = userService.getUserList(pageable);

		PageResponse<UserResponse> response = new PageResponse<>(
			results.currentPage(),
			results.pageSize(),
			results.totalPages(),
			results.totalElements(),
			results.sortBy(),
			results.isAsc(),
			results.items().stream()
				.map(UserResponse::from)
				.toList()
		);

		return ResponseEntity.ok(response);
	}

	@PutMapping("/{userId}")
	@RequireRole(UserRole.MASTER)
	public ResponseEntity<UpdateUserResponse> updateUser(
		@PathVariable UUID userId,
		@RequestBody UpdateUserRequest request
	) {
		UpdateUserCommand command = new UpdateUserCommand(
			request.password(),
			request.role(),
			request.slackId()
		);

		UserResult result = userService.updateUser(userId, command);

		return ResponseEntity.ok(new UpdateUserResponse(result.userId(),"사용자 정보가 수정되었습니다."));
	}
}
