package com.nowayback.order.application.event;

import com.nowayback.common.event.EventType;
import com.nowayback.order.application.event.payload.OrderPaymentEventPayload;
import com.nowayback.order.application.event.payload.StockDecreaseEventPayload;
import com.nowayback.order.application.event.payload.StockDecreaseFailedEventPayload;
import com.nowayback.order.application.event.payload.StockDecreaseSucceedEventPayload;
import com.nowayback.order.application.event.payload.OrderCreatedEventPayload;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderEventType implements EventType {
    ORDER_CREATED(
        "ORDER_CREATED",
        OrderCreatedEventPayload.class,
        "order-events"
    ),
    STOCK_DECREASE(
        "STOCK_DECREASE",
        StockDecreaseEventPayload.class,
        "order-stock-decrease"
    ),
    STOCK_DECREASE_SUCCEEDED(
        "STOCK_DECREASE_SUCCEED",
        StockDecreaseSucceedEventPayload.class,
        "order-stock-decrease-succeed"
    ),
    STOCK_DECREASE_FAILED(
        "STOCK_DECREASE_FAILED",
        StockDecreaseFailedEventPayload.class,
        "order-stock-decrease-failed"
    ),
    ORDER_PAYMENT(
        "ORDER_PAYMENT",
        OrderPaymentEventPayload.class,
        "order-payment"
    )
    ;

    private final String type;
    private final Class<?> payloadClass;
    private final String topic;

    public static OrderEventType from(String typeString) {
        return valueOf(typeString.toUpperCase());
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Class<?> getPayloadClass() {
        return payloadClass;
    }

    @Override
    public String getTopic() {
        return topic;
    }
}
