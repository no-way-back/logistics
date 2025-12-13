package com.nowayback.order.application.event.payload;

import com.nowayback.common.event.EventPayload;
import com.nowayback.order.domain.entity.Order;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StockIncreaseEventPayload implements EventPayload {
    private UUID orderId;
    private List<StockIncreaseEventPayload.OrderItemLine> items;

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

    public StockIncreaseEventPayload(UUID orderId, List<StockIncreaseEventPayload.OrderItemLine> items) {
        this.orderId = orderId;
        this.items = items;
    }

    public static StockIncreaseEventPayload from(Order order) {
        return new StockIncreaseEventPayload(
            order.getId(),
            order.getOrderItems().stream()
                .map(orderItem -> new OrderItemLine(
                    orderItem.getProductId().getId(),
                    orderItem.getQuantity())
                )
                .toList()
        );
    }
}
