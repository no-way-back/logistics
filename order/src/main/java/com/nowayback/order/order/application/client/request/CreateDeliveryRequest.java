package com.nowayback.order.order.application.client.request;

import java.util.UUID;

public record CreateDeliveryRequest(
    UUID orderId,
    UUID sourceHubId,
    UUID destinationHubId,
    String deliveryAddress,
    String recipientName,
    String recipientSlackId
) {

    public static CreateDeliveryRequest of(
        UUID orderId,
        UUID sourceHubId,
        UUID destinationHubId,
        String deliveryAddress,
        String recipientName,
        String recipientSlackId
    ) {
        return new CreateDeliveryRequest(
            orderId,
            sourceHubId,
            destinationHubId,
            deliveryAddress,
            recipientName,
            recipientSlackId
        );
    }
}
