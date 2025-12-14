package com.nowayback.order.infrastructure.messaging.kafka;

import com.nowayback.common.event.Event;
import com.nowayback.common.event.Event.EventRaw;
import com.nowayback.common.event.EventPayload;
import com.nowayback.order.application.event.EventDispatcher;
import com.nowayback.order.application.event.OrderEventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventConsumer {

    private final EventDispatcher eventDispatcher;

    @KafkaListener(topics = "order-payment-succeeded", groupId = "${spring.kafka.consumer.group-id}")
    public void onPaymentSucceeded(String message, Acknowledgment ack) {
        log.info("[PaymentEventConsumer.onPaymentSucceeded] {}", message);
        Event<? extends EventPayload> event = getEvent(message);

        eventDispatcher.dispatch(event);
        ack.acknowledge();
    }

    @KafkaListener(topics = "order-payment-failed", groupId = "${spring.kafka.consumer.group-id}")
    public void onPaymentFailed(String message, Acknowledgment ack) {
        log.info("[PaymentEventConsumer.onPaymentFailed] {}", message);
        Event<? extends EventPayload> event = getEvent(message);

        eventDispatcher.dispatch(event);
        ack.acknowledge();
    }

    private static Event<? extends EventPayload> getEvent(String message) {
        EventRaw raw = Event.toRaw(message);
        String typeString = raw.getType();

        OrderEventType eventType = OrderEventType.from(typeString);

        Event<? extends EventPayload> event = Event.fromJson(message, eventType);
        return event;
    }
}
