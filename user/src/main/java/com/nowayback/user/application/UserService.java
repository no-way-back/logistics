package com.nowayback.user.application;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nowayback.user.application.dto.command.LoginUserCommand;
import com.nowayback.user.application.dto.command.SignupUserCommand;
import com.nowayback.user.application.dto.result.LoginResult;
import com.nowayback.user.application.dto.result.UserResult;
import com.nowayback.user.domain.entity.User;
import com.nowayback.user.domain.entity.UserStatus;
import com.nowayback.user.domain.repository.UserRepository;
import com.nowayback.user.exception.UserErrorCode;
import com.nowayback.user.exception.UserException;

import exception.GlobalException;

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
		if (userRepository.existsByUsernameAndDeletedAtNull(command.username())) {
			throw new UserException(UserErrorCode.USER_ALREADY_EXISTS);
		}

		User user = User.createUser(
			command.username(),
			passwordEncoder.encode(command.password()),
			command.role(),
			command.slackId()
			);

		userRepository.save(user);
		return UserResult.from(user);
	}

	@Transactional(readOnly = true)
	public LoginResult login(LoginUserCommand command) {
		User user = userRepository.findByUsernameAndDeletedAtIsNull(command.username())
			.orElseThrow(() -> new GlobalException(UserErrorCode.USER_NOT_FOUND));

		if (!passwordEncoder.matches(command.password(), user.getPassword())) {
			throw new GlobalException(UserErrorCode.INVALID_PASSWORD);
		}

		if (user.getStatus() != UserStatus.APPROVED) {
			throw new GlobalException(UserErrorCode.USER_NOT_APPROVED);
		}

		return LoginResult.from(user);
	}

	@Transactional
	public void approveSignup(UUID userId, UUID approvedBy) {
		User user = userRepository.findByUserIdAndDeletedAtIsNull(userId)
			.orElseThrow(() -> new GlobalException(UserErrorCode.USER_NOT_FOUND));

		user.approveSignup();
	}
}
