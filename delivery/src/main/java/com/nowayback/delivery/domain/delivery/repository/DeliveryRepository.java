package com.nowayback.delivery.domain.delivery.repository;

import com.nowayback.delivery.domain.delivery.entity.Delivery;
import com.nowayback.delivery.domain.delivery.vo.OrderId;

import java.util.Optional;
import java.util.UUID;

public interface DeliveryRepository {
    Delivery save(Delivery delivery);
    boolean existsByOrderId(OrderId orderId);
    Optional<Delivery> findById(UUID deliveryUuid);
}
