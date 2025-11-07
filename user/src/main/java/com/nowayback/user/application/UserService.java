package com.nowayback.user.application;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nowayback.user.application.dto.command.ApprovalCommand;
import com.nowayback.user.application.dto.command.LoginUserCommand;
import com.nowayback.user.application.dto.command.SignupUserCommand;
import com.nowayback.user.application.dto.result.LoginResult;
import com.nowayback.user.application.dto.result.UserResult;
import com.nowayback.user.application.exception.UserApplicationErrorCode;
import com.nowayback.user.application.exception.UserApplicationException;
import com.nowayback.user.domain.entity.User;
import com.nowayback.user.domain.entity.UserStatus;
import com.nowayback.user.domain.repository.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional
	public UserResult signup(SignupUserCommand command) {
		validateUsernameNotDuplicated(command.username());

		User user = User.createUser(
			command.username(),
			passwordEncoder.encode(command.password()),
			command.role(),
			command.slackId()
		);

		User savedUser = userRepository.save(user);
		return UserResult.from(savedUser);
	}

	@Transactional(readOnly = true)
	public LoginResult login(LoginUserCommand command) {
		User user = findActiveUserByUsername(command.username());

		validatePassword(command.password(), user.getPassword());

		user.validateCanLogin();

		return LoginResult.from(user);
	}

	@Transactional
	public UserResult approveOrRejectSignup(ApprovalCommand command) {
		validateApprovalStatus(command.status());

		User user = findActiveUserById(command.userId());

		if (command.status() == UserStatus.APPROVED) {
			user.approveSignup();
		} else {
			user.rejectSignup();
		}

		User savedUser = userRepository.save(user);
		return UserResult.from(savedUser);
	}

	// ========== Private Helper Methods ==========

	private User findActiveUserById(UUID userId) {
		return userRepository.findByUserIdAndDeletedAtIsNull(userId)
			.orElseThrow(() -> new UserApplicationException(UserApplicationErrorCode.USER_NOT_FOUND));
	}

	private User findActiveUserByUsername(String username) {
		return userRepository.findByUsernameAndDeletedAtIsNull(username)
			.orElseThrow(() -> new UserApplicationException(UserApplicationErrorCode.USER_NOT_FOUND));
	}

	private void validateUsernameNotDuplicated(String username) {
		if (userRepository.existsByUsernameAndDeletedAtIsNull(username)) {
			throw new UserApplicationException(UserApplicationErrorCode.USER_ALREADY_EXISTS);
		}
	}

	private void validatePassword(String rawPassword, String encodedPassword) {
		if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
			throw new UserApplicationException(UserApplicationErrorCode.INVALID_PASSWORD);
		}
	}

	private void validateApprovalStatus(UserStatus status) {
		if (status != UserStatus.APPROVED && status != UserStatus.REJECTED) {
			throw new UserApplicationException(UserApplicationErrorCode.INVALID_APPROVAL_STATUS);
		}
	}
}
