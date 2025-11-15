package com.nowayback.order.order.presentation.response;

import com.nowayback.order.order.application.dto.OrderCreateResult;
import java.util.UUID;

public record OrderCreateResponse(
    UUID orderId
) {

    public static OrderCreateResponse from(OrderCreateResult result) {
        return new OrderCreateResponse(result.orderId());
    }
}
