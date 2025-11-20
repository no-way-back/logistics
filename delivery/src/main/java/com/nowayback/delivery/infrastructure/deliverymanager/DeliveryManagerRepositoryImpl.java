package com.nowayback.delivery.infrastructure.deliverymanager;

import com.nowayback.delivery.domain.deliverymanager.entity.DeliveryManager;
import com.nowayback.delivery.domain.deliverymanager.repository.DeliveryManagerRepository;
import com.nowayback.delivery.domain.deliverymanager.vo.DeliveryManagerType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class DeliveryManagerRepositoryImpl implements DeliveryManagerRepository {

    private final DeliveryManagerJpaRepository deliveryManagerJpaRepository;

    @Override
    public DeliveryManager save(DeliveryManager deliveryManager) {
        return deliveryManagerJpaRepository.save(deliveryManager);
    }

    @Override
    public boolean existsById(UUID userId) {
        return deliveryManagerJpaRepository.existsByIdAndDeletedAtIsNull(userId);
    }

    @Override
    public Integer findMaxSequenceByType(DeliveryManagerType type) {
        return deliveryManagerJpaRepository.findMaxSequenceByType(type);
    }

    @Override
    public Optional<DeliveryManager> findById(UUID deliveryManagerId) {
        return deliveryManagerJpaRepository.findByIdAndDeletedAtIsNull(deliveryManagerId);
    }

    @Override
    public List<DeliveryManager> findAllByTypeOrderBySequenceAsc(DeliveryManagerType type) {
        return deliveryManagerJpaRepository.findAllActiveByTypeOrderBySequenceAsc(type);
    }
}
