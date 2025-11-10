package com.nowayback.order.application.command;

import com.nowayback.order.domain.vo.CustomerId;
import java.util.UUID;

public record CancelOrderCommand(
    CustomerId customerId,
    UUID orderId
) {
    public static CancelOrderCommand of(CustomerId customerId, UUID orderId) {
        return new CancelOrderCommand(customerId, orderId);
    }
}
