package com.nowayback.order.order.application.dto;

import java.util.UUID;

public record OrderCreateResult(
    UUID orderId
) {
    public static OrderCreateResult of(UUID orderId) {
        return new OrderCreateResult(orderId);
    }
}
