package com.nowayback.order.infrastructure.persistence;

import com.nowayback.order.domain.event.entity.SagaStatus;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SagaStatusJpaRepository extends JpaRepository<SagaStatus, UUID> {

}
