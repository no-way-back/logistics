package com.nowayback.delivery.domain.deliverymanager.repository;

import com.nowayback.delivery.domain.deliverymanager.entity.DeliveryManager;
import com.nowayback.delivery.domain.deliverymanager.vo.DeliveryManagerType;
import com.nowayback.delivery.domain.deliverymanager.vo.DeliverySequence;

import java.util.Optional;
import java.util.UUID;

public interface DeliveryManagerRepository {
    DeliveryManager save(DeliveryManager deliveryManager);
    Integer findMaxSequenceByType(DeliveryManagerType type);
    Optional<DeliveryManager> findById(UUID deliveryManagerId);
}
