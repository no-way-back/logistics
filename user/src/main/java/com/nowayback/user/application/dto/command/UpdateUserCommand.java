package com.nowayback.user.application.dto.command;

import com.nowayback.common.security.annotation.UserRole;

public record UpdateUserCommand(
	String password,
	UserRole role,
	String slackId
) {
}