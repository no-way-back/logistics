package com.nowayback.delivery.infrastructure.repository;

import com.nowayback.delivery.domain.deliveryroute.entity.DeliveryRoute;
import com.nowayback.delivery.domain.deliveryroute.repository.DeliveryRouteRepository;
import com.nowayback.delivery.domain.deliveryroute.vo.DeliveryId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class DeliveryRouteRepositoryImpl implements DeliveryRouteRepository {

    private final DeliveryRouteJpaRepository deliveryRouteJpaRepository;

    @Override
    public List<DeliveryRoute> saveAll(List<DeliveryRoute> deliveryRoutes) {
        return deliveryRouteJpaRepository.saveAll(deliveryRoutes);
    }

    @Override
    public Optional<DeliveryRoute> findById(UUID deliveryRouteId) {
        return deliveryRouteJpaRepository.findByIdAndDeletedAtIsNull(deliveryRouteId);
    }

    @Override
    public Page<DeliveryRoute> findAllByDeliveryId(DeliveryId deliveryId, Pageable pageable) {
        if (deliveryId == null) {
            return deliveryRouteJpaRepository.findAllByDeletedAtIsNull(pageable);
        } else {
            return deliveryRouteJpaRepository.findAllByDeliveryIdAndDeletedAtIsNull(deliveryId, pageable);
        }
    }
}
