package com.nowayback.order.domain.repository;

import com.nowayback.common.event.EventType;
import com.nowayback.order.domain.event.entity.ProcessedEvents;
import java.util.UUID;

public interface ProcessedEventsRepository {
    int insertIgnore(UUID eventId, String eventType);

    ProcessedEvents save(ProcessedEvents processedEvents);
}
