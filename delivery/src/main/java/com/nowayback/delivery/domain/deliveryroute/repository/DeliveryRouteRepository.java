package com.nowayback.delivery.domain.deliveryroute.repository;

import com.nowayback.delivery.domain.deliveryroute.entity.DeliveryRoute;
import com.nowayback.delivery.domain.deliveryroute.vo.DeliveryId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeliveryRouteRepository {
    List<DeliveryRoute> saveAll(List<DeliveryRoute> deliveryRoutes);
    Optional<DeliveryRoute> findById(UUID deliveryRouteId);
    Page<DeliveryRoute> findAllByDeliveryId(DeliveryId of, Pageable pageable);
}
