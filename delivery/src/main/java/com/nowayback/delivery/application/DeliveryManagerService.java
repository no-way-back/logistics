package com.nowayback.delivery.application;

import com.nowayback.delivery.application.command.CreateDeliveryManagerCommand;
import com.nowayback.delivery.application.dto.DeliveryManagerResult;
import com.nowayback.delivery.application.exception.DeliveryManagerApplicationErrorCode;
import com.nowayback.delivery.application.exception.DeliveryManagerApplicationException;
import com.nowayback.delivery.application.service.UserClient;
import com.nowayback.delivery.domain.deliverymanager.entity.DeliveryManager;
import com.nowayback.delivery.domain.deliverymanager.repository.DeliveryManagerRepository;
import com.nowayback.delivery.domain.deliverymanager.vo.DeliveryManagerType;
import com.nowayback.delivery.domain.deliverymanager.vo.DeliverySequence;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryManagerService {

    private final DeliveryManagerRepository deliveryManagerRepository;
    private final UserClient userClient;

    @Transactional
    public DeliveryManagerResult createDeliveryManager(CreateDeliveryManagerCommand command) {
        validateDuplicateDeliveryManagerId(command.userId());

        DeliveryManagerType type = command.type();
        DeliverySequence sequence = DeliverySequence.of(getMaxDeliveryManagerSequence(type));

        String slackId = userClient.getUserInfoById(command.userId()).slackId();

        DeliveryManager deliveryManager = DeliveryManager.create(
                command.userId(),
                command.hubId(),
                type,
                slackId,
                sequence
        );

        DeliveryManager savedDeliveryManager = deliveryManagerRepository.save(deliveryManager);
        return DeliveryManagerResult.from(savedDeliveryManager);
    }

    @Transactional(readOnly = true)
    public DeliveryManagerResult getDeliveryManager(UUID deliveryManagerId) {
        DeliveryManager deliveryManager = getDeliveryManagerById(deliveryManagerId);
        return DeliveryManagerResult.from(deliveryManager);
    }

    @Transactional
    public void deleteDeliveryManager(UUID actorId, UUID deliveryManagerId) {
        DeliveryManager deliveryManager = getDeliveryManagerById(deliveryManagerId);
        deliveryManager.delete(actorId);
    }

    private DeliveryManager getDeliveryManagerById(UUID deliveryManagerId) {
        return deliveryManagerRepository.findById(deliveryManagerId)
                .orElseThrow(() -> new DeliveryManagerApplicationException(DeliveryManagerApplicationErrorCode.NOT_FOUND_DELIVERY_MANAGER));
    }

    private int getMaxDeliveryManagerSequence(DeliveryManagerType type) {
        Integer maxSequence = deliveryManagerRepository.findMaxSequenceByType(type);
        return maxSequence != null ? maxSequence : 0;
    }

    private void validateDuplicateDeliveryManagerId(UUID userId) {
        if (deliveryManagerRepository.findById(userId).isPresent()) {
            throw new DeliveryManagerApplicationException(DeliveryManagerApplicationErrorCode.DUPLICATE_DELIVERY_MANAGER_ID);
        }
    }
}
