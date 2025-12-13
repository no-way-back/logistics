package com.nowayback.order.application.event;

import com.nowayback.common.event.AggregateType;
import com.nowayback.common.event.Event;
import com.nowayback.order.application.event.payload.StockIncreaseEventPayload;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StockIncreaseEvent extends Event<StockIncreaseEventPayload> {

    public static StockIncreaseEvent of(UUID orderId, StockIncreaseEventPayload payload) {
        StockIncreaseEvent event = new StockIncreaseEvent();
        event.eventId = UUID.randomUUID();
        event.aggregateId = orderId;
        event.aggregateType = AggregateType.ORDER;
        event.type = OrderEventType.STOCK_INCREASE;
        event.payload = payload;
        return event;
    }
}
