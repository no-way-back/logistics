package com.nowayback.product.application.event;

import com.nowayback.common.event.EventType;
import com.nowayback.product.application.event.payload.StockDecreaseEventPayload;
import com.nowayback.product.application.event.payload.StockDecreaseFailedEventPayload;
import com.nowayback.product.application.event.payload.StockDecreaseSucceedEventPayload;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductEventType implements EventType {
    STOCK_DECREASE(
        "STOCK_DECREASE",
        StockDecreaseEventPayload.class,
        "order-stock-decrease"
    ),
    STOCK_DECREASE_SUCCEEDED(
        "STOCK_DECREASE_SUCCEEDED",
        StockDecreaseSucceedEventPayload.class,
        "order-stock-decrease-succeeded"
    ),
    STOCK_DECREASE_FAILED(
        "STOCK_DECREASE_FAILED",
        StockDecreaseFailedEventPayload.class,
        "order-stock-decrease-failed"
    );

    private final String type;
    private final Class<?> payloadClass;
    private final String topic;

    public static ProductEventType from(String typeString) {
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
