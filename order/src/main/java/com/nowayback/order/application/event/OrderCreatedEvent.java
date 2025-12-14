package com.nowayback.order.application.event;

import com.nowayback.common.event.AggregateType;
import com.nowayback.common.event.Event;
import com.nowayback.order.application.event.payload.OrderCreatedEventPayload;
import java.util.UUID;
import lombok.ToString;

@ToString
public class OrderCreatedEvent extends Event<OrderCreatedEventPayload> {
    public static OrderCreatedEvent of(UUID aggregateId, OrderCreatedEventPayload payload) {
        OrderCreatedEvent event = new OrderCreatedEvent();
        event.eventId = UUID.randomUUID();
        event.aggregateId = aggregateId;
        event.type = OrderEventType.ORDER_CREATED;
        event.aggregateType = AggregateType.ORDER;
        event.payload = payload;
        return event;
    }
}
