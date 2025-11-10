package com.nowayback.user.presentation.dto.request;

import com.nowayback.common.security.annotation.UserRole;

public record UpdateUserRequest(
	String password,
	UserRole role,
	String slackId
) {
}