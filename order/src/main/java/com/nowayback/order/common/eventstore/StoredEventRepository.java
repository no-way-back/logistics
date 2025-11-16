package com.nowayback.order.common.eventstore;

import com.nowayback.order.common.eventstore.vo.AggregateType;
import com.nowayback.order.common.eventstore.vo.EventType;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoredEventRepository extends JpaRepository<StoredEvent, UUID> {

    Page<StoredEvent> findByEventType(EventType eventType, Pageable pageable);

    Page<StoredEvent> findByAggregateType(AggregateType aggregateType, Pageable pageable);
}
