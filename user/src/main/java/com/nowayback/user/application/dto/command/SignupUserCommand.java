package com.nowayback.user.application.dto.command;

import com.nowayback.common.security.annotation.UserRole;

public record SignupUserCommand(
	String username,
	String password,
	UserRole role,
	String slackId
) {
}
