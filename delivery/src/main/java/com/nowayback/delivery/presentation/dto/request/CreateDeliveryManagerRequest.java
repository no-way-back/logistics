package com.nowayback.delivery.presentation.dto.request;

import com.nowayback.delivery.domain.deliverymanager.vo.DeliveryManagerType;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateDeliveryManagerRequest(
        @NotNull UUID userId,
        @NotNull UUID hubId,
        @NotNull DeliveryManagerType type
) {
}
