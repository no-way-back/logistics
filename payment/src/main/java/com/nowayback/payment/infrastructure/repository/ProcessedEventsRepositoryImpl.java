package com.nowayback.payment.infrastructure.repository;

import com.nowayback.payment.domain.ProcessedEventsRepository;
import com.nowayback.payment.domain.event.ProcessedEvents;
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
