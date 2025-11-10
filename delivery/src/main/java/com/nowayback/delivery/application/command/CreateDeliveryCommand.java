package com.nowayback.delivery.application.command;

import com.nowayback.delivery.presentation.dto.request.CreateDeliveryRequest;

import java.util.UUID;

public record CreateDeliveryCommand (
        UUID orderId,
        UUID sourceHubId,
        UUID destinationHubId,
        String deliveryAddress,
        String recipientName,
        String recipientSlackId
) {

    public static CreateDeliveryCommand from(CreateDeliveryRequest request) {
        return new CreateDeliveryCommand(
                request.orderId(),
                request.sourceHubId(),
                request.destinationHubId(),
                request.deliveryAddress(),
                request.recipientName(),
                request.recipientSlackId()
        );
    }
}
