package com.nowayback.payment.infrastructure.messaging.kafka;

import com.nowayback.common.event.Event;
import com.nowayback.common.event.Event.EventRaw;
import com.nowayback.common.event.EventPayload;
import com.nowayback.payment.application.event.EventDispatcher;
import com.nowayback.payment.application.event.PaymentEventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventConsumer {

    private final EventDispatcher eventDispatcher;

    @KafkaListener(topics = "order-payment", groupId = "${spring.kafka.consumer.group-id}")
    public void onOrderPayment(String message, Acknowledgment ack) {
        log.info("[OrderEventConsumer.onOrderPayment] {}", message);
        Event<? extends EventPayload> event = getEvent(message);

        eventDispatcher.dispatch(event);

        ack.acknowledge();
    }

    private static Event<? extends EventPayload> getEvent(String message) {
        EventRaw raw = Event.toRaw(message);
        String typeString = raw.getType();

        PaymentEventType eventType = PaymentEventType.from(typeString);

        Event<? extends EventPayload> event = Event.fromJson(message, eventType);
        return event;
    }
}
