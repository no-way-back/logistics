package com.nowayback.delivery.application.deliverymanager;

import com.nowayback.delivery.application.deliverymanager.command.CreateDeliveryManagerCommand;
import com.nowayback.delivery.application.deliverymanager.dto.DeliveryManagerResult;
import com.nowayback.delivery.application.deliverymanager.exception.DeliveryManagerApplicationErrorCode;
import com.nowayback.delivery.application.deliverymanager.exception.DeliveryManagerApplicationException;
import com.nowayback.delivery.application.service.UserClient;
import com.nowayback.delivery.domain.deliverymanager.entity.DeliveryManager;
import com.nowayback.delivery.domain.deliverymanager.repository.DeliveryManagerRepository;
import com.nowayback.delivery.domain.deliverymanager.vo.DeliveryManagerType;
import com.nowayback.delivery.domain.deliverymanager.vo.DeliverySequence;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class DeliveryManagerService {

    private final DeliveryManagerRepository deliveryManagerRepository;
    private final UserClient userClient;

    private final Map<DeliveryManagerType, Integer> roundRobinIndex = new ConcurrentHashMap<>();

    @Transactional
    public DeliveryManagerResult createDeliveryManager(CreateDeliveryManagerCommand command) {
        validateDuplicateDeliveryManagerId(command.userId());

        DeliveryManagerType type = command.type();
        DeliverySequence sequence = DeliverySequence.of(getMaxDeliveryManagerSequence(type) + 1);

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

    @Transactional(readOnly = true)
    public UUID assignDeliveryManager(DeliveryManagerType type) {
        List<DeliveryManager> managers = deliveryManagerRepository.findAllByTypeOrderBySequenceAsc(type);

        if (managers.isEmpty()) {
            throw new DeliveryManagerApplicationException(DeliveryManagerApplicationErrorCode.NOT_FOUND_DELIVERY_MANAGER);
        }

        int index = roundRobinIndex
                .compute(type, (key, value) -> value == null ? 0 : (value + 1) % managers.size());

        return managers.get(index).getId();
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
        if (deliveryManagerRepository.existsById(userId)) {
            throw new DeliveryManagerApplicationException(DeliveryManagerApplicationErrorCode.DUPLICATE_DELIVERY_MANAGER_ID);
        }
    }
}
