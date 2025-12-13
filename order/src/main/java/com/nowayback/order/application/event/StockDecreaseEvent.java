package com.nowayback.order.application.event;

import com.nowayback.common.event.AggregateType;
import com.nowayback.common.event.Event;
import com.nowayback.order.application.event.payload.StockDecreaseEventPayload;
import java.util.UUID;
import lombok.ToString;

@ToString
public class StockDecreaseEvent extends Event<StockDecreaseEventPayload> {

    public static StockDecreaseEvent of(UUID sagaId, UUID orderId, StockDecreaseEventPayload payload) {
        StockDecreaseEvent event = new StockDecreaseEvent();
        event.sagaId = sagaId;
        event.eventId = UUID.randomUUID();
        event.aggregateId = orderId;
        event.aggregateType = AggregateType.ORDER;
        event.type = OrderEventType.STOCK_DECREASE;
        event.payload = payload;
        return event;
    }
}
