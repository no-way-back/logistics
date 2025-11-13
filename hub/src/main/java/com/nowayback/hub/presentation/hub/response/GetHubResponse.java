package com.nowayback.hub.presentation.hub.response;

import com.nowayback.hub.application.hub.dto.GetHubResult;
import com.nowayback.hub.domain.hub.entity.Hub;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record GetHubResponse(
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
    public static GetHubResponse from(GetHubResult result) {
        return new GetHubResponse(
                result.hubId(),
                result.name(),
                result.address(),
                result.latitude(),
                result.longitude(),
                result.createdAt(),
                result.createdBy(),
                result.updatedAt(),
                result.updatedBy()
        );
    }
    public static Page<GetHubResponse> from(Page<GetHubResult> results) {
        return results.map(GetHubResponse::from);
    }
}
