package com.nowayback.user.presentation.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.nowayback.user.application.dto.result.UserResult;
import com.nowayback.user.domain.entity.UserRole;
import com.nowayback.user.domain.entity.UserStatus;

public record UserResponse(
	UUID userId,
	String username,
	UserRole role,
	UserStatus status,
	String slackId,
	LocalDateTime createdAt
) {
	public static UserResponse from(UserResult result) {
		return new UserResponse(
			result.userId(),
			result.username(),
			result.role(),
			result.status(),
			result.slackId(),
			result.createdAt()
		);
	}
}
