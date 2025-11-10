package com.nowayback.delivery.application.dto;

import com.nowayback.delivery.domain.delivery.entity.Delivery;
import com.nowayback.delivery.domain.delivery.vo.DeliveryStatus;

import java.util.UUID;

public record DeliveryResult(
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

    public static DeliveryResult from(Delivery delivery) {
        return new DeliveryResult(
                delivery.getId(),
                delivery.getOrderId().getId(),
                delivery.getStatus(),
                delivery.getSourceHubId().getId(),
                delivery.getDestinationHubId().getId(),
                delivery.getRecipientInfo().getAddress(),
                delivery.getRecipientInfo().getName(),
                delivery.getRecipientInfo().getSlackId(),
                delivery.getCompanyDeliveryManagerId().getId()
        );
    }
}
