package com.nowayback.delivery.application.deliverymanager.command;

import com.nowayback.delivery.domain.deliverymanager.vo.DeliveryManagerType;
import com.nowayback.delivery.domain.deliverymanager.vo.HubId;

import java.util.UUID;

public record CreateDeliveryManagerCommand(
        UUID userId,
        HubId hubId,
        DeliveryManagerType type
) {

    public static CreateDeliveryManagerCommand of(
            UUID userId,
            UUID hubId,
            DeliveryManagerType type
    ) {
        return new CreateDeliveryManagerCommand(
                userId,
                HubId.of(hubId),
                type
        );
    }
}