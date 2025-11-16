package com.nowayback.order.common.eventstore;

import com.nowayback.order.common.eventstore.vo.AggregateType;
import com.nowayback.order.common.eventstore.vo.EventType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_event_store")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoredEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @Enumerated(EnumType.STRING)
    private AggregateType aggregateType;
    private UUID aggregateId;

    @Lob
    private String payload;

    private LocalDateTime occurredAt;
    private LocalDateTime storedAt;

    private StoredEvent(
        EventType eventType,
        AggregateType aggregateType,
        UUID aggregateId,
        String payload,
        LocalDateTime occurredAt,
        LocalDateTime storedAt
    ) {
        this.eventType = eventType;
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.payload = payload;
        this.occurredAt = occurredAt;
        this.storedAt = storedAt;
    }

    public static StoredEvent of(
        EventType eventType,
        AggregateType aggregateType,
        UUID aggregateId,
        String payload,
        LocalDateTime occurredAt
    ) {
        return new StoredEvent(
            eventType,
            aggregateType,
            aggregateId,
            payload,
            occurredAt,
            LocalDateTime.now()
        );
    }
}

