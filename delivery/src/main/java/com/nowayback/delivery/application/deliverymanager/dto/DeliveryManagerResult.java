package com.nowayback.delivery.application.deliverymanager.dto;

import com.nowayback.delivery.domain.deliverymanager.entity.DeliveryManager;
import com.nowayback.delivery.domain.deliverymanager.vo.DeliveryManagerType;

import java.util.UUID;

public record DeliveryManagerResult(
        UUID deliveryManagerId,
        UUID hubId,
        DeliveryManagerType type,
        String slackId,
        int sequence
) {

    public static DeliveryManagerResult from(DeliveryManager deliveryManager) {
        return new DeliveryManagerResult(
                deliveryManager.getId(),
                deliveryManager.getHubId().getId(),
                deliveryManager.getType(),
                deliveryManager.getSlackId(),
                deliveryManager.getDeliverySequence().getSequence()
        );
    }
}
