package com.nowayback.order.order.infrastructure.client.feign.request;

import com.nowayback.order.order.application.client.request.CreateDeliveryRequest;
import java.util.UUID;

public record CreateDeliveryFeignRequest(
    UUID orderId,
    UUID sourceHubId,
    UUID destinationHubId,
    String deliveryAddress,
    String recipientName,
    String recipientSlackId
) {

    public static CreateDeliveryFeignRequest from(CreateDeliveryRequest request) {

        return new CreateDeliveryFeignRequest(
            request.orderId(),
            request.sourceHubId(),
            request.destinationHubId(),
            request.deliveryAddress(),
            request.recipientName(),
            request.recipientSlackId()
        );
    }
}
