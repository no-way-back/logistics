package com.nowayback.delivery.presentation.deliverymanager.dto.response;

import com.nowayback.delivery.application.deliverymanager.dto.DeliveryManagerResult;
import com.nowayback.delivery.domain.deliverymanager.vo.DeliveryManagerType;

import java.util.UUID;

public record DeliveryManagerResponse(
        UUID deliveryManagerId,
        UUID hubId,
        DeliveryManagerType type,
        String slackId,
        int sequence
) {

    public static DeliveryManagerResponse from(DeliveryManagerResult result) {
        return new DeliveryManagerResponse(
                result.deliveryManagerId(),
                result.hubId(),
                result.type(),
                result.slackId(),
                result.sequence()
        );
    }
}
