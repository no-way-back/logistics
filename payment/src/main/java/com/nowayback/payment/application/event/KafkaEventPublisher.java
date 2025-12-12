package com.nowayback.payment.application.event;

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
        ).whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("[KafkaEventPublisher.publish] 전송 실패 topic={}, key={}, eventId={}",
                    event.getType().getTopic(),
                    event.getAggregateId(),
                    event.getEventId(),
                    ex
                );
            } else {
                log.info("[KafkaEventPublisher.publish] 전송 성공 topic={}, partition={}, offset={}, key={}, eventId={}",
                    result.getRecordMetadata().topic(),
                    result.getRecordMetadata().partition(),
                    result.getRecordMetadata().offset(),
                    event.getAggregateId(),
                    event.getEventId()
                );
            }
        });;
    }
}