package com.nowayback.hub.application.hub.dto;

import com.nowayback.hub.domain.hub.entity.Hub;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record GetHubResult(
        UUID hubId,
        String name,
        String address,
        BigDecimal latitude,
        BigDecimal longitude,
        LocalDateTime createdAt,
        UUID createdBy,
        LocalDateTime updatedAt,
        UUID updatedBy
) {
    public static GetHubResult of(Hub hub) {
        return new GetHubResult(
                hub.getId(),
                hub.getName(),
                hub.getAddress(),
                hub.getLatitude(),
                hub.getLongitude(),
                hub.getCreatedAt(),
                hub.getCreatedBy(),
                hub.getUpdatedAt(),
                hub.getUpdatedBy()
        );
    }

    public static Page<GetHubResult> of(Page<Hub> hubs) {
        return hubs.map(GetHubResult::of);
    }
}
