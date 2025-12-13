package com.nowayback.order.application.event.payload;

import com.nowayback.common.event.EventPayload;
import com.nowayback.order.application.event.payload.StockDecreaseEventPayload.OrderItemLine;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StockIncreaseSucceedEventPayload implements EventPayload {

    private UUID orderId;
    private List<OrderItemLine> items;
    private LocalDateTime occurredAt;

    public StockIncreaseSucceedEventPayload(
        UUID orderId,
        List<OrderItemLine> items,
        LocalDateTime occurredAt
    ) {
        this.orderId = orderId;
        this.items = items;
        this.occurredAt = occurredAt;
    }

    public static StockIncreaseSucceedEventPayload from(StockDecreaseEventPayload payload) {
        return new StockIncreaseSucceedEventPayload(
            payload.getOrderId(),
            payload.getItems(),
            LocalDateTime.now()
        );
    }
}