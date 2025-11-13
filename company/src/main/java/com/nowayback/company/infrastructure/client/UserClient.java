package com.nowayback.company.infrastructure.client;

import java.util.UUID;

import com.nowayback.company.infrastructure.client.dto.UserResponse;

public interface UserClient {

	UserResponse getUser(UUID userId);
}
