package com.nowayback.order.domain.repository;

import com.nowayback.order.domain.event.entity.SagaStatus;
import java.util.Optional;
import java.util.UUID;

public interface SagaStatusRepository {
    Optional<SagaStatus> findById(UUID id);
    SagaStatus save(SagaStatus sagaStatus);
}
