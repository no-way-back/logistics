package com.nowayback.user.presentation.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.nowayback.user.application.dto.result.UserResult;
import com.nowayback.user.domain.entity.UserStatus;

import com.nowayback.common.security.annotation.UserRole;

public record UserResponse(
	UUID userId,
	String username,
	UserRole role,
	UserStatus status,
	String slackId,
	LocalDateTime createdAt,
	String message
) {
	public static UserResponse from(UserResult result) {
		return new UserResponse(
			result.userId(),
			result.username(),
			result.role(),
			result.status(),
			result.slackId(),
			result.createdAt(),
			null
		);
	}

	// 메시지를 포함한 응답 생성
	public static UserResponse withMessage(UserResult result, String message) {
		return new UserResponse(
			result.userId(),
			result.username(),
			result.role(),
			result.status(),
			result.slackId(),
			result.createdAt(),
			message
		);
	}
}
