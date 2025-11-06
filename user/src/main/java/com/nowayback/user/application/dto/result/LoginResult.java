package com.nowayback.user.application.dto.result;

import java.util.UUID;

import com.nowayback.user.domain.entity.User;
import com.nowayback.user.domain.entity.UserRole;

public record LoginResult(
	UUID userId,
	String username,
	UserRole role
) {
	public static LoginResult from(User user) {
		return new LoginResult(
			user.getUserId(),
			user.getUsername(),
			user.getRole()
		);
	}
}
