package com.nowayback.product.application.event.payload;

import com.nowayback.common.event.EventPayload;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StockIncreaseFailedEventPayload implements EventPayload {

    private UUID orderId;
    private List<StockIncreaseEventPayload.OrderItemLine> items;
    private String reason;
    private LocalDateTime occurredAt;

    public StockIncreaseFailedEventPayload(
        UUID orderId,
        List<StockIncreaseEventPayload.OrderItemLine> items,
        String reason,
        LocalDateTime occurredAt
    ) {
        this.orderId = orderId;
        this.items = items;
        this.reason = reason;
        this.occurredAt = occurredAt;
    }

    public static StockIncreaseFailedEventPayload from(
        StockIncreaseEventPayload payload,
        String reason
    ) {
        return new StockIncreaseFailedEventPayload(
            payload.getOrderId(),
            payload.getItems(),
            reason,
            LocalDateTime.now()
        );
    }
}
