package com.nowayback.company.infrastructure.client.dto;

import java.util.UUID;

import com.nowayback.common.security.annotation.UserRole;

public record UserResponse(
	UUID userId,
	String username,
	UserRole role
) {
}
