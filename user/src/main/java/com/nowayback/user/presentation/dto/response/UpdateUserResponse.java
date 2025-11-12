package com.nowayback.user.presentation.dto.response;

import java.util.UUID;

public record UpdateUserResponse(
	UUID userId,
	String message
) {
}