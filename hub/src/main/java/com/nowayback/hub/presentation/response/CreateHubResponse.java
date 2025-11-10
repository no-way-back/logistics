package com.nowayback.hub.presentation.response;

import com.nowayback.hub.application.dto.CreateHubResult;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CreateHubResponse(
        UUID hubId,
        String name,
        String address,
        BigDecimal latitude,
        BigDecimal longitude,
        LocalDateTime createdAt,
        UUID createdBy
) {
    public static CreateHubResponse from(CreateHubResult result) {
        return new CreateHubResponse(
                result.hubId(),
                result.name(),
                result.address(),
                result.latitude(),
                result.longitude(),
                result.createdAt(),
                result.createdBy()
        );
    }
}