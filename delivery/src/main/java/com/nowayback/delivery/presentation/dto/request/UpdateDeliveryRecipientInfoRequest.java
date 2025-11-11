package com.nowayback.delivery.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateDeliveryRecipientInfoRequest(
        @NotBlank String deliveryAddress,
        @NotBlank String recipientName,
        @NotBlank String recipientSlackId
) {
}
