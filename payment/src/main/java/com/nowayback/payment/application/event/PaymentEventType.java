package com.nowayback.payment.application.event;

import com.nowayback.common.event.EventType;
import com.nowayback.payment.application.event.paylaod.OrderPaymentEventPayload;
import com.nowayback.payment.application.event.paylaod.OrderPaymentFailedEventPayload;
import com.nowayback.payment.application.event.paylaod.OrderPaymentSucceededEventPayload;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PaymentEventType implements EventType {

    ORDER_PAYMENT(
        "ORDER_PAYMENT",
        OrderPaymentEventPayload.class,
        "order-payment"
    ),
    ORDER_PAYMENT_SUCCEEDED(
        "ORDER_PAYMENT_SUCCEEDED",
        OrderPaymentSucceededEventPayload.class,
        "order-payment-succeeded"
    ),
    ORDER_PAYMENT_FAILED(
        "ORDER_PAYMENT_FAILED",
        OrderPaymentFailedEventPayload.class,
        "order-payment-failed"
    )
    ;
    private final String type;
    private final Class<?> payloadClass;
    private final String topic;

    public static PaymentEventType from(String typeString) {
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
