package com.nowayback.order.domain.repository;

import com.nowayback.order.domain.event.entity.ProcessedEvents;
import java.util.UUID;

public interface ProcessedEventsRepository {
    boolean existById(UUID eventId);

    ProcessedEvents save(ProcessedEvents processedEvents);
}
