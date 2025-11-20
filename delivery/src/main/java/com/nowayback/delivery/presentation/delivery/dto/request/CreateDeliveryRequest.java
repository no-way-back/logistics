package com.nowayback.delivery.presentation.delivery.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateDeliveryRequest (
        @NotNull UUID orderId,
        @NotNull UUID sourceHubId,
        @NotNull UUID destinationHubId,
        @NotBlank String deliveryAddress,
        @NotBlank String recipientName,
        @NotBlank String recipientSlackId
) {
}
