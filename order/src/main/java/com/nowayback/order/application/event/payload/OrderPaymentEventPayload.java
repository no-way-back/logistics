package com.nowayback.order.application.event.payload;

import com.nowayback.common.event.EventPayload;
import java.util.UUID;
import lombok.Getter;

@Getter
public class OrderPaymentEventPayload implements EventPayload {
    private UUID orderId;
    private UUID userId;
    private Long amount;

    public static OrderPaymentEventPayload create(UUID orderId, UUID userId, Long amount) {
        OrderPaymentEventPayload payload = new OrderPaymentEventPayload();
        payload.orderId = orderId;
        payload.userId = userId;
        payload.amount = amount;
        return payload;
    }
}
