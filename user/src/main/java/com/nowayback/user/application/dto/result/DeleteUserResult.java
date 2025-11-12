package com.nowayback.user.application.dto.result;

import java.util.UUID;

public record DeleteUserResult(
	UUID userId
) {
	public static DeleteUserResult from(UUID userId) {
		return new DeleteUserResult(userId);
	}
}
