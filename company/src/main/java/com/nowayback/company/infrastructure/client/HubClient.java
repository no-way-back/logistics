package com.nowayback.company.infrastructure.client;

import java.util.UUID;

import com.nowayback.company.infrastructure.client.dto.HubResponse;

public interface HubClient {

	HubResponse getHub(UUID hubId);

	HubResponse getHubByManager(UUID managerId);
}
