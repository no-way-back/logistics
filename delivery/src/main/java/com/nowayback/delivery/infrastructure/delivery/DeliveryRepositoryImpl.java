package com.nowayback.delivery.infrastructure.delivery;

import com.nowayback.delivery.domain.delivery.entity.Delivery;
import com.nowayback.delivery.domain.delivery.repository.DeliveryRepository;
import com.nowayback.delivery.domain.delivery.vo.DeliveryStatus;
import com.nowayback.delivery.domain.delivery.vo.HubId;
import com.nowayback.delivery.domain.delivery.vo.OrderId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class DeliveryRepositoryImpl implements DeliveryRepository {

    private final DeliveryJpaRepository deliveryJpaRepository;
    private final DeliveryCustomRepository deliveryCustomRepository;

    @Override
    public Delivery save(Delivery delivery) {
        return deliveryJpaRepository.save(delivery);
    }

    @Override
    public boolean existsByOrderId(OrderId orderId) {
        return deliveryJpaRepository.existsByOrderIdAndDeletedAtIsNull(orderId);
    }

    @Override
    public Optional<Delivery> findById(UUID deliveryId) {
        return deliveryJpaRepository.findByIdAndDeletedAtIsNull(deliveryId);
    }

    @Override
    public Page<Delivery> searchDeliveries(OrderId orderId, HubId sourceHubId, HubId destinationHubId, DeliveryStatus status, int page, int size) {
        return deliveryCustomRepository.searchDeliveries(orderId, sourceHubId, destinationHubId, status, page, size);
    }
}
