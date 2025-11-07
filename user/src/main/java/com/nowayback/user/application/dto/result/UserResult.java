package com.nowayback.user.application.dto.result;

import java.time.LocalDateTime;
import java.util.UUID;

import com.nowayback.user.domain.entity.User;
import com.nowayback.user.domain.entity.UserStatus;

import com.nowayback.common.security.annotation.UserRole;

public record UserResult(
	UUID userId,
	String username,
	UserRole role,
	UserStatus status,
	String slackId,
	LocalDateTime createdAt
) {
	public static UserResult from(User user) {
		return new UserResult(
			user.getUserId(),
			user.getUsername(),
			user.getRole(),
			user.getStatus(),
			user.getSlackId(),
			user.getCreatedAt()
		);
	}
}
