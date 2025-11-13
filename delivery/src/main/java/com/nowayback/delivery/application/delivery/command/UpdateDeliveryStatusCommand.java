package com.nowayback.delivery.application.delivery.command;

import com.nowayback.delivery.domain.delivery.vo.DeliveryStatus;

public record UpdateDeliveryStatusCommand (
        DeliveryStatus status
) {

    public static UpdateDeliveryStatusCommand of(
            DeliveryStatus status
    ) {
        return new UpdateDeliveryStatusCommand(status);
    }
}
