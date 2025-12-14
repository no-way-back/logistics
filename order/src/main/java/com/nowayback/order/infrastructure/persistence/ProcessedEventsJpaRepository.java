package com.nowayback.order.infrastructure.persistence;

import com.nowayback.order.domain.event.entity.ProcessedEvents;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProcessedEventsJpaRepository extends JpaRepository<ProcessedEvents, UUID> {

    @Modifying
    @Query(value = """
        INSERT INTO order_service.processed_events (event_id, event_type, received_at)
        VALUES (:eventId, :eventType, NOW())
        ON CONFLICT (event_id) DO NOTHING
        """, nativeQuery = true)
    int insertIgnore(
        @Param("eventId") UUID eventId,
        @Param("eventType") String eventType
    );
}
