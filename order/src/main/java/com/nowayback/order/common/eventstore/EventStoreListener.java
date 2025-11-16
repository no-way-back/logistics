package com.nowayback.order.common.eventstore;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nowayback.order.common.event.DomainEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventStoreListener {

    private final StoredEventRepository storedEventRepository;
    private final ObjectMapper objectMapper;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleEvent(DomainEvent event) throws JsonProcessingException {
        String payload = objectMapper.writeValueAsString(event);

        StoredEvent storedEvent = StoredEvent.of(
            event.getEventType(),
            event.getAggregateType(),
            event.getAggregateId(),
            payload,
            event.getOccurredAt()
        );

        storedEventRepository.save(storedEvent);
        log.info("Event stored: {} - {}", storedEvent.getEventType(), storedEvent.getAggregateId());
    }
}
