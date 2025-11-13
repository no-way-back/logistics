package com.nowayback.company.infrastructure.client.feign;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.nowayback.company.infrastructure.client.HubClient;
import com.nowayback.company.infrastructure.client.UserClient;
import com.nowayback.company.infrastructure.client.dto.HubResponse;
import com.nowayback.company.infrastructure.client.dto.UserResponse;

@FeignClient(
	name = "user-service",
	path = "/users"
)
public interface UserFeignClient extends UserClient {

	@Override
	@GetMapping("/{userId}")
	UserResponse getUser(@PathVariable("userId") UUID userId);
}
