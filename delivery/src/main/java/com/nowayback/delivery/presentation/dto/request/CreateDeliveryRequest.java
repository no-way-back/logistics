package com.nowayback.delivery.presentation.dto.request;

import com.nowayback.delivery.application.command.CreateDeliveryCommand;

import java.util.UUID;

public record CreateDeliveryRequest (
        UUID orderId,
        UUID sourceHubId,
        UUID destinationHubId,
        String deliveryAddress,
        String recipientName,
        String recipientSlackId
) {
}
