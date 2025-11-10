package com.nowayback.delivery.application.command;

import java.util.UUID;

public record CreateDeliveryCommand (
        UUID orderId,
        UUID sourceHubId,
        UUID destinationHubId,
        String deliveryAddress,
        String recipientName,
        String recipientSlackId
) {
}
