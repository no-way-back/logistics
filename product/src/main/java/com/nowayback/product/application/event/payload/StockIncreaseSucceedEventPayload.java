package com.nowayback.product.application.event.payload;

import com.nowayback.common.event.EventPayload;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StockIncreaseSucceedEventPayload implements EventPayload {

    private UUID orderId;
    private List<StockIncreaseEventPayload.OrderItemLine> items;
    private LocalDateTime occurredAt;

    public StockIncreaseSucceedEventPayload(
        UUID orderId,
        List<StockIncreaseEventPayload.OrderItemLine> items,
        LocalDateTime occurredAt
    ) {
        this.orderId = orderId;
        this.items = items;
        this.occurredAt = occurredAt;
    }

    public static StockIncreaseSucceedEventPayload from(StockIncreaseEventPayload payload) {
        return new StockIncreaseSucceedEventPayload(
            payload.getOrderId(),
            payload.getItems(),
            LocalDateTime.now()
        );
    }
}
