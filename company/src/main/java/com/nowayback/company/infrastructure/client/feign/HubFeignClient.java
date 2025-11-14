package com.nowayback.company.infrastructure.client.feign;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.nowayback.company.infrastructure.client.HubClient;
import com.nowayback.company.infrastructure.client.dto.HubResponse;

@FeignClient(
	name = "hub-service",
	path = "/hubs"
)
public interface HubFeignClient extends HubClient {

	@Override
	@GetMapping("/{hubId}")
	HubResponse getHub(@PathVariable("hubId") UUID hubId);

	@Override
	@GetMapping("/manager")
	HubResponse getHubByManager(@RequestParam("managerId") UUID managerId);
}
