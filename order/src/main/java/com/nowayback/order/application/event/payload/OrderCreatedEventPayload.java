package com.nowayback.order.application.event.payload;

import com.nowayback.common.event.EventPayload;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderCreatedEventPayload implements EventPayload {

    private UUID orderId;
    private List<OrderItemLine> items;

    @Getter
    @AllArgsConstructor
    public static class OrderItemLine {
        private UUID productId;
        private Integer quantity;
    }

    private OrderCreatedEventPayload() {}

    public static OrderCreatedEventPayload create(UUID orderId, List<OrderItemLine> items) {
        return new OrderCreatedEventPayload(orderId, items);
    }
}
