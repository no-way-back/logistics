package com.nowayback.delivery.application.delivery.service;

import java.util.List;
import java.util.UUID;

public interface HubClient {
    boolean existsById(UUID hubId);
    HubRoutesInfo getHubRoutesInfo(UUID originHubId, UUID destinationHubId);

    record HubRoutesInfo(
            UUID originHubId,
            String originHubName,
            UUID destinationHubId,
            String destinationHubName,
            int totalDistanceM,
            int totalDurationMin,
            List<RouteSegment> segments
    ) {
    }

    record RouteSegment(
            int sequence,
            UUID fromHubId,
            String fromHubName,
            UUID toHubId,
            String toHubName,
            int distanceM,
            int estimatedDurationMin
    ){}
}
