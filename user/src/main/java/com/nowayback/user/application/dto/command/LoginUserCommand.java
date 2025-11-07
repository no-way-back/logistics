package com.nowayback.user.application.dto.command;

public record LoginUserCommand(
	String username,
	String password
) {
}
