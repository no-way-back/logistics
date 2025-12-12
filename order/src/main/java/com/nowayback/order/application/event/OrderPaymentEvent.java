package com.nowayback.order.application.event;

import com.nowayback.common.event.AggregateType;
import com.nowayback.common.event.Event;
import com.nowayback.order.application.event.payload.OrderPaymentEventPayload;
import java.util.UUID;
import lombok.ToString;

@ToString
public class OrderPaymentEvent extends Event<OrderPaymentEventPayload> {
    public static OrderPaymentEvent create(OrderPaymentEventPayload payload) {
        OrderPaymentEvent event = new OrderPaymentEvent();
        event.eventId = UUID.randomUUID();
        event.aggregateId = payload.getOrderId();
        event.type = OrderEventType.ORDER_PAYMENT;
        event.aggregateType = AggregateType.ORDER;
        event.payload = payload;
        return event;
    }
}
