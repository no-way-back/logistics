package com.nowayback.delivery.presentation.dto.response;

import com.nowayback.delivery.application.delivery.dto.DeliveryResult;
import com.nowayback.delivery.domain.delivery.vo.DeliveryStatus;

import java.util.UUID;

public record DeliveryResponse(
        UUID deliveryId,
        UUID orderId,
        DeliveryStatus status,
        UUID sourceHubId,
        UUID destinationHubId,
        String deliveryAddress,
        String recipientName,
        String recipientSlackId,
        UUID companyDeliveryManagerId
) {

    public static DeliveryResponse from(DeliveryResult result) {
        return new DeliveryResponse(
                result.deliveryId(),
                result.orderId(),
                result.status(),
                result.sourceHubId(),
                result.destinationHubId(),
                result.deliveryAddress(),
                result.recipientName(),
                result.recipientSlackId(),
                result.companyDeliveryManagerId()
        );
    }
}
