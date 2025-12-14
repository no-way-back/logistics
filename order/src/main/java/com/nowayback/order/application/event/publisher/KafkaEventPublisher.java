package com.nowayback.order.application.event.publisher;

import com.nowayback.common.event.Event;
import com.nowayback.common.event.EventPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public <T extends EventPayload> void publish(Event<T> event) {
        log.info("[KafkaEventPublisher.publish] event = {}", event);

        kafkaTemplate.send(
            event.getType().getTopic(),
            event.getAggregateId().toString(),
            event
        );
    }
}
