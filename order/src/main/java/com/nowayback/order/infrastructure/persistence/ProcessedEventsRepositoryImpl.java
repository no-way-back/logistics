package com.nowayback.order.infrastructure.persistence;

import com.nowayback.common.event.EventType;
import com.nowayback.order.domain.event.entity.ProcessedEvents;
import com.nowayback.order.domain.repository.ProcessedEventsRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProcessedEventsRepositoryImpl implements ProcessedEventsRepository {

    private final ProcessedEventsJpaRepository processedEventsJpaRepository;

    @Override
    public int insertIgnore(UUID eventId, String eventType) {
        return processedEventsJpaRepository.insertIgnore(eventId, eventType);
    }

    @Override
    public ProcessedEvents save(ProcessedEvents processedEvents) {
        return processedEventsJpaRepository.save(processedEvents);
    }
}
