package com.nowayback.payment.domain;

import com.nowayback.payment.domain.event.ProcessedEvents;
import java.util.UUID;

public interface ProcessedEventsRepository {
    boolean existById(UUID eventId);

    ProcessedEvents save(ProcessedEvents processedEvents);
}
