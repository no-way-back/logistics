package com.nowayback.product.infrastructure.processedvents.repository;

import com.nowayback.product.domain.event.ProcessedEvents;
import com.nowayback.product.domain.event.ProcessedEventsRepository;
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
