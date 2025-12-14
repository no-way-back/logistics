package com.nowayback.order.application.event.payload;

import com.nowayback.common.event.EventPayload;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StockDecreaseEventPayload implements EventPayload {

    private UUID orderId;
    private List<OrderItemLine> items;

    @Getter
    @NoArgsConstructor
    public static class OrderItemLine {
        private UUID productId;
        private int quantity;

        public OrderItemLine(UUID productId, int quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }
    }

    public StockDecreaseEventPayload(UUID orderId, List<OrderItemLine> items) {
        this.orderId = orderId;
        this.items = items;
    }

    public static StockDecreaseEventPayload from(OrderCreatedEventPayload payload) {
        return new StockDecreaseEventPayload(
            payload.getOrderId(),
            payload.getItems().stream()
                .map(i -> new OrderItemLine(i.getProductId(), i.getQuantity()))
                .toList()
        );
    }
}
