package com.nowayback.delivery.infrastructure.repository;

import com.nowayback.delivery.domain.delivery.entity.Delivery;
import com.nowayback.delivery.domain.delivery.vo.OrderId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DeliveryJpaRepository extends JpaRepository<Delivery, UUID> {
    boolean existsByOrderIdAndDeletedAtIsNull(OrderId orderId);
    Optional<Delivery> findByIdAndDeletedAtIsNull(UUID id);
}
