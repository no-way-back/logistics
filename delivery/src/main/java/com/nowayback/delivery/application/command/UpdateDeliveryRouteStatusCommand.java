package com.nowayback.delivery.application.command;

import com.nowayback.delivery.domain.deliveryroute.vo.DeliveryRouteStatus;

public record UpdateDeliveryRouteStatusCommand (
        DeliveryRouteStatus status
) {

    public static UpdateDeliveryRouteStatusCommand of(
            DeliveryRouteStatus status
    ) {
        return new UpdateDeliveryRouteStatusCommand(status);
    }
}
