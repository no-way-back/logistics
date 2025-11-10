package com.nowayback.hub.presentation.response;

import com.nowayback.hub.application.dto.UpdateHubResult;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record UpdateHubResponse(
        UUID hubId,
        String name,
        String address,
        BigDecimal latitude,
        BigDecimal longitude,
        LocalDateTime updateAt,
        UUID updatedBy
) {
    public static UpdateHubResponse from(UpdateHubResult result) {
        return new UpdateHubResponse(
                result.hubId(),
                result.name(),
                result.address(),
                result.latitude(),
                result.longitude(),
                result.updateAt(),
                result.updatedBy()
        );
    }
}
