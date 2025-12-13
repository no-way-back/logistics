package com.nowayback.order.infrastructure.persistence;

import com.nowayback.order.domain.event.entity.ProcessedEvents;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessedEventsJpaRepository extends JpaRepository<ProcessedEvents, UUID> {

}
