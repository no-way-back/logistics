package com.nowayback.product.infrastructure.messaging.kafka;

import com.nowayback.common.dataserializer.DataSerializer;
import com.nowayback.common.event.Event;
import com.nowayback.common.event.Event.EventRaw;
import com.nowayback.common.event.EventPayload;
import com.nowayback.product.application.event.EventDispatcher;
import com.nowayback.product.application.event.ProductEventType;
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

    @KafkaListener(topics = "order-stock-decrease", groupId = "${spring.kafka.consumer.group-id}")
    public void onDecreaseStock(String message, Acknowledgment ack) {
        log.info("[OrderEventConsumer.onDecreaseStock] message={}", message);

        Event<? extends EventPayload> event = getEvent(message);

        eventDispatcher.dispatch(event);

        ack.acknowledge();
    }

    private static Event<? extends EventPayload> getEvent(String message) {
        EventRaw raw = Event.toRaw(message);
        String typeString = raw.getType();

        ProductEventType eventType = ProductEventType.from(typeString);

        Event<? extends EventPayload> event = Event.fromJson(message, eventType);
        return event;
    }
}
