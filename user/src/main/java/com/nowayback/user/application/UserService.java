package com.nowayback.user.application;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nowayback.user.application.dto.command.ApprovalCommand;
import com.nowayback.user.application.dto.command.LoginUserCommand;
import com.nowayback.user.application.dto.command.SignupUserCommand;
import com.nowayback.user.application.dto.result.LoginResult;
import com.nowayback.user.application.dto.result.UserResult;
import com.nowayback.user.domain.entity.User;
import com.nowayback.user.domain.entity.UserStatus;
import com.nowayback.user.domain.repository.UserRepository;
import com.nowayback.user.exception.UserErrorCode;
import com.nowayback.user.exception.UserException;

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
		if (userRepository.existsByUsernameAndDeletedAtIsNull(command.username())) {
			throw new UserException(UserErrorCode.USER_ALREADY_EXISTS);
		}

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
		User user = userRepository.findByUsernameAndDeletedAtIsNull(command.username())
			.orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

		if (!passwordEncoder.matches(command.password(), user.getPassword())) {
			throw new UserException(UserErrorCode.INVALID_PASSWORD);
		}

		if (user.getStatus() != UserStatus.APPROVED) {
			throw new UserException(UserErrorCode.USER_NOT_APPROVED);
		}

		return LoginResult.from(user);
	}

	@Transactional
	public UserResult approveOrRejectSignup(ApprovalCommand command) {
		if (command.status() != UserStatus.APPROVED && command.status() != UserStatus.REJECTED) {
			throw new UserException(UserErrorCode.INVALID_USER_STATUS);
		}

		User user = userRepository.findByUserIdAndDeletedAtIsNull(command.userId())
			.orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

		if (user.getStatus() != UserStatus.PENDING) {
			throw new UserException(UserErrorCode.ALREADY_PROCESSED);
		}

		if (command.status() == UserStatus.APPROVED) {
			user.approveSignup();
		} else {
			user.rejectSignup();
		}

		User savedUser = userRepository.save(user);

		return UserResult.from(savedUser);
	}
}
