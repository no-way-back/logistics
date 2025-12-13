package com.nowayback.product.infrastructure.processedvents.repository;

import com.nowayback.product.domain.event.ProcessedEvents;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessedEventsJpaRepository extends JpaRepository<ProcessedEvents, UUID> {

}
