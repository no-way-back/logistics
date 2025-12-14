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
public class StockDecreaseFailedEventPayload implements EventPayload {

    private UUID orderId;
    private List<OrderItemLine> items;
    private String reason;
    private LocalDateTime occurredAt;

    public StockDecreaseFailedEventPayload(
        UUID orderId,
        List<StockDecreaseEventPayload.OrderItemLine> items,
        String reason,
        LocalDateTime occurredAt
    ) {
        this.orderId = orderId;
        this.items = items;
        this.reason = reason;
        this.occurredAt = occurredAt;
    }

    public static StockDecreaseFailedEventPayload from(
        StockDecreaseEventPayload payload,
        String reason
    ) {
        return new StockDecreaseFailedEventPayload(
            payload.getOrderId(),
            payload.getItems(),
            reason,
            LocalDateTime.now()
        );
    }
}
