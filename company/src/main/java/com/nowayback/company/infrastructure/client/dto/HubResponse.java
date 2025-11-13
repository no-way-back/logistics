package com.nowayback.company.infrastructure.client.dto;

import java.util.UUID;

public record HubResponse(
	UUID hubId,
	String hubName,
	UUID managerId,
	String address
) {
}
