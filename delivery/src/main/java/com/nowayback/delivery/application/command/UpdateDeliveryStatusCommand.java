package com.nowayback.delivery.application.command;

import com.nowayback.delivery.domain.delivery.vo.DeliveryStatus;
import com.nowayback.delivery.presentation.dto.request.UpdateDeliveryStatusRequest;

public record UpdateDeliveryStatusCommand (
        DeliveryStatus status
) {

    public static UpdateDeliveryStatusCommand from(UpdateDeliveryStatusRequest request) {
        return new UpdateDeliveryStatusCommand(request.status());
    }
}
