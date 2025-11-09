package com.nowayback.delivery.domain.delivery.repository;

import com.nowayback.delivery.domain.delivery.entity.Delivery;
import com.nowayback.delivery.domain.delivery.vo.DeliveryStatus;
import com.nowayback.delivery.domain.delivery.vo.HubId;
import com.nowayback.delivery.domain.delivery.vo.OrderId;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface DeliveryRepository {
    Delivery save(Delivery delivery);
    boolean existsByOrderId(OrderId orderId);
    Optional<Delivery> findById(UUID deliveryUuid);
    Page<Delivery> searchDeliveries(OrderId orderId, HubId sourceHubId, HubId destinationHubId, DeliveryStatus status, int page, int size);
}
