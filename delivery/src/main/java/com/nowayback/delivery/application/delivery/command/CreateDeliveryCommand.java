package com.nowayback.delivery.application.delivery.command;

import com.nowayback.delivery.domain.delivery.vo.HubId;
import com.nowayback.delivery.domain.delivery.vo.OrderId;
import com.nowayback.delivery.domain.delivery.vo.RecipientInfo;

import java.util.UUID;

public record CreateDeliveryCommand (
        OrderId orderId,
        HubId sourceHubId,
        HubId destinationHubId,
        RecipientInfo recipientInfo
) {

    public static CreateDeliveryCommand of(
            UUID orderId,
            UUID sourceHubId,
            UUID destinationHubId,
            String deliveryAddress,
            String recipientName,
            String recipientSlackId
    ) {
        return new CreateDeliveryCommand(
                OrderId.of(orderId),
                HubId.of(sourceHubId),
                HubId.of(destinationHubId),
                RecipientInfo.of(deliveryAddress, recipientName, recipientSlackId)
        );
    }
}
