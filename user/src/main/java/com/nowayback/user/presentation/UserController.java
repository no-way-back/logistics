package com.nowayback.user.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nowayback.user.application.UserService;
import com.nowayback.user.application.dto.command.LoginUserCommand;
import com.nowayback.user.application.dto.command.SignupUserCommand;
import com.nowayback.user.application.dto.result.LoginResult;
import com.nowayback.user.application.dto.result.UserResult;
import com.nowayback.user.infrastructure.security.JwtTokenProvider;
import com.nowayback.user.presentation.dto.request.LoginUserRequest;
import com.nowayback.user.presentation.dto.request.SignupUserRequest;
import com.nowayback.user.presentation.dto.response.LoginResponse;
import com.nowayback.user.presentation.dto.response.UserResponse;

import jakarta.validation.Valid;

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
			.body(UserResponse.from(result));
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
}
