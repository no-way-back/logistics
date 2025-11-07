package com.nowayback.common.security.annotation;

import java.util.UUID;

public record AuthUser(
	UUID userId,
	String username,
	UserRole role
) {}
