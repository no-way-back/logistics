package com.nowayback.delivery.application.command;

import com.nowayback.delivery.domain.delivery.vo.DeliveryStatus;

public record UpdateDeliveryStatusCommand (
        DeliveryStatus status
) {
}
