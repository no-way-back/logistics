package com.nowayback.order.common.event;

import com.nowayback.order.common.eventstore.vo.AggregateType;
import com.nowayback.order.common.eventstore.vo.EventType;
import java.time.LocalDateTime;
import java.util.UUID;

public interface DomainEvent {
    UUID getAggregateId();

    // 이벤트가 발생한 시점
    LocalDateTime getOccurredAt();

    AggregateType getAggregateType();
    EventType getEventType();
}