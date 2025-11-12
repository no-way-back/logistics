package com.nowayback.order.application.client.response;

import java.util.UUID;

public record CreateDeliveryResponse(
    UUID deliveryId,
    UUID orderId,
    String status,
    UUID sourceHubId,
    UUID destinationHubId,
    String deliveryAddress,
    String recipientName,
    String recipientSlackId,
    UUID companyDeliveryManagerId
) {

    // Delivery 서비스의 상태 문자열을 안전하게 매핑하기 위한 팩토리
    public static CreateDeliveryResponse of(
        UUID deliveryId,
        UUID orderId,
        String status,
        UUID sourceHubId,
        UUID destinationHubId,
        String deliveryAddress,
        String recipientName,
        String recipientSlackId,
        UUID companyDeliveryManagerId
    ) {
        return new CreateDeliveryResponse(
            deliveryId,
            orderId,
            status,
            sourceHubId,
            destinationHubId,
            deliveryAddress,
            recipientName,
            recipientSlackId,
            companyDeliveryManagerId
        );
    }
}
