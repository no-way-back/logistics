package com.nowayback.user.application.dto.command;

import com.nowayback.user.domain.entity.UserRole;

public record SignupUserCommand(
	String username,
	String password,
	UserRole role,
	String slackId
) {
}
