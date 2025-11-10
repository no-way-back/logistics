package com.nowayback.hub.application.hub.dto;

import com.nowayback.hub.domain.hub.entity.Hub;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record UpdateHubResult (
        UUID hubId,
        String name,
        String address,
        BigDecimal latitude,
        BigDecimal longitude,
        LocalDateTime updateAt,
        UUID updatedBy
) {
    public static UpdateHubResult of(Hub hub) {
        return new UpdateHubResult(
                hub.getId(),
                hub.getName(),
                hub.getAddress(),
                hub.getLatitude(),
                hub.getLongitude(),
                hub.getUpdatedAt(),
                hub.getUpdatedBy()
        );
    }
}
