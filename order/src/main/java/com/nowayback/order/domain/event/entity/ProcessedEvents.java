package com.nowayback.order.domain.event.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@AllArgsConstructor
@Table(name = "processed_events")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProcessedEvents {

    @Id
    @Column(name = "event_id", length = 100)
    private String eventId;

    @Column(name = "event_type", nullable = false, length = 100)
    private String eventType;

    @Column(name = "source_service", nullable = false, length = 50)
    private String sourceService;

    @Column(name = "payload_hash", length = 255)
    private String payloadHash;

    @Column(name = "received_at", nullable = false)
    private LocalDateTime receivedAt;

    public static ProcessedEvents of(
        String eventId,
        String eventType,
        String sourceService,
        String payloadHash
    ) {
        return ProcessedEvents.builder()
            .eventId(eventId)
            .eventType(eventType)
            .sourceService(sourceService)
            .payloadHash(payloadHash)
            .receivedAt(LocalDateTime.now())
            .build();
    }
}

