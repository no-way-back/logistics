package com.nowayback.delivery.infrastructure.deliverymanager;

import com.nowayback.delivery.domain.deliverymanager.entity.DeliveryManager;
import com.nowayback.delivery.domain.deliverymanager.vo.DeliveryManagerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeliveryManagerJpaRepository extends JpaRepository<DeliveryManager, UUID> {

    boolean existsByIdAndDeletedAtIsNull(UUID userId);

    @Query("SELECT COALESCE(MAX(dm.deliverySequence.sequence), 0) " +
            "FROM DeliveryManager dm " +
            "WHERE dm.type = :type AND dm.deletedAt IS NULL")
    Integer findMaxSequenceByType(DeliveryManagerType type);

    Optional<DeliveryManager> findByIdAndDeletedAtIsNull(UUID deliveryManagerId);

    @Query("SELECT dm " +
            "FROM DeliveryManager dm " +
            "WHERE dm.type = :type AND dm.deletedAt IS NULL " +
            "ORDER BY dm.deliverySequence.sequence ASC")
    List<DeliveryManager> findAllActiveByTypeOrderBySequenceAsc(DeliveryManagerType type);
}
