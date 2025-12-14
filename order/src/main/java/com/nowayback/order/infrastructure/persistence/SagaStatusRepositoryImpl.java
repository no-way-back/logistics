package com.nowayback.order.infrastructure.persistence;

import com.nowayback.order.domain.event.entity.SagaStatus;
import com.nowayback.order.domain.repository.SagaStatusRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SagaStatusRepositoryImpl implements SagaStatusRepository {

    private final SagaStatusJpaRepository sagaStatusJpaRepository;

    @Override
    public Optional<SagaStatus> findById(UUID id) {
        return sagaStatusJpaRepository.findById(id);
    }

    @Override
    public SagaStatus save(SagaStatus sagaStatus) {
        return sagaStatusJpaRepository.save(sagaStatus);
    }
}
