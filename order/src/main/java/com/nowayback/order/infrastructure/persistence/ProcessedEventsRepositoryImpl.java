package com.nowayback.order.infrastructure.persistence;

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
    public boolean existById(UUID eventId) {
        return processedEventsJpaRepository.existsById(eventId);
    }

    @Override
    public ProcessedEvents save(ProcessedEvents processedEvents) {
        return processedEventsJpaRepository.save(processedEvents);
    }
}
