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
public class StockDecreaseSucceedEventPayload implements EventPayload {

    private UUID orderId;
    private List<OrderItemLine> items;
    private LocalDateTime occurredAt;

    public StockDecreaseSucceedEventPayload(
        UUID orderId,
        List<StockDecreaseEventPayload.OrderItemLine> items,
        LocalDateTime occurredAt
    ) {
        this.orderId = orderId;
        this.items = items;
        this.occurredAt = occurredAt;
    }

    public static StockDecreaseSucceedEventPayload from(StockDecreaseEventPayload payload) {
        return new StockDecreaseSucceedEventPayload(
            payload.getOrderId(),
            payload.getItems(),
            LocalDateTime.now()
        );
    }
}