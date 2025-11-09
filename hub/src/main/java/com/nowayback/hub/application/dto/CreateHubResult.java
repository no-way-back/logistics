package com.nowayback.hub.application.dto;

import com.nowayback.hub.domain.entity.Hub;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CreateHubResult(
        UUID hubId,
        String name,
        String address,
        BigDecimal latitude,
        BigDecimal longitude,
        LocalDateTime createdAt,
        UUID createdBy
) {
    public static CreateHubResult from(Hub hub) {
        return new CreateHubResult(
                hub.getId(),
                hub.getName(),
                hub.getAddress(),
                hub.getLatitude(),
                hub.getLongitude(),
                hub.getCreatedAt(),
                hub.getCreatedBy()
        );
    }
}
