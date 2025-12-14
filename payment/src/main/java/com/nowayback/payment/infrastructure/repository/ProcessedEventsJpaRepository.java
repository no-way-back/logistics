package com.nowayback.payment.infrastructure.repository;

import com.nowayback.payment.domain.event.ProcessedEvents;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessedEventsJpaRepository extends JpaRepository<ProcessedEvents, UUID> {

}
