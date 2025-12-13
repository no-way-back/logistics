package com.nowayback.product.domain.event;

import java.util.UUID;

public interface ProcessedEventsRepository {
    boolean existById(UUID eventId);

    ProcessedEvents save(ProcessedEvents processedEvents);
}
