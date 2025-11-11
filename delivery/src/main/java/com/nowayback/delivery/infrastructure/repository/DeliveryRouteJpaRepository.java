package com.nowayback.delivery.infrastructure.repository;

import com.nowayback.delivery.domain.deliveryroute.entity.DeliveryRoute;
import com.nowayback.delivery.domain.deliveryroute.vo.DeliveryId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DeliveryRouteJpaRepository extends JpaRepository<DeliveryRoute, UUID> {
    Optional<DeliveryRoute> findByIdAndDeletedAtIsNull(UUID deliveryRouteId);
    Page<DeliveryRoute> findAllByDeliveryIdAndDeletedAtIsNull(DeliveryId deliveryId, Pageable pageable);
}
