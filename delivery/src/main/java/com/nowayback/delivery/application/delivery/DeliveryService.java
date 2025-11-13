package com.nowayback.delivery.application.delivery;

import com.nowayback.delivery.application.delivery.command.CreateDeliveryCommand;
import com.nowayback.delivery.application.delivery.command.UpdateDeliveryRecipientInfoCommand;
import com.nowayback.delivery.application.delivery.command.UpdateDeliveryStatusCommand;
import com.nowayback.delivery.application.delivery.dto.DeliveryResult;
import com.nowayback.delivery.application.delivery.exception.DeliveryApplicationErrorCode;
import com.nowayback.delivery.application.delivery.exception.DeliveryApplicationException;
import com.nowayback.delivery.application.delivery.service.HubClient;
import com.nowayback.delivery.application.deliverymanager.DeliveryManagerService;
import com.nowayback.delivery.domain.delivery.entity.Delivery;
import com.nowayback.delivery.domain.delivery.repository.DeliveryRepository;
import com.nowayback.delivery.domain.delivery.vo.*;
import com.nowayback.delivery.domain.deliverymanager.vo.DeliveryManagerType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryManagerService deliveryManagerService;
    private final HubClient hubClient;

    @Transactional
    public DeliveryResult createDelivery(CreateDeliveryCommand command) {
        validateDuplicateOrderId(command.orderId());
        validateHubExists(command.sourceHubId());
        validateHubExists(command.destinationHubId());

        DeliveryManagerId companyDeliveryManagerId = assignDeliveryManager();

        Delivery delivery = Delivery.create(
                command.orderId(),
                command.sourceHubId(),
                command.destinationHubId(),
                command.recipientInfo(),
                companyDeliveryManagerId
        );

        Delivery savedDelivery = deliveryRepository.save(delivery);

        // TODO: 배송 경로 생성 로직 추가 필요

        return DeliveryResult.from(savedDelivery);
    }

    private DeliveryManagerId assignDeliveryManager() {
        try {
            return DeliveryManagerId.of(deliveryManagerService.assignDeliveryManager(DeliveryManagerType.COMPANY));
        } catch (Exception e) {
            throw new DeliveryApplicationException(DeliveryApplicationErrorCode.FAILED_TO_ASSIGN_DELIVERY_MANAGER);
        }
    }

    @Transactional(readOnly = true)
    public DeliveryResult getDelivery(UUID deliveryId) {
        Delivery delivery = getDeliveryById(deliveryId);
        return DeliveryResult.from(delivery);
    }

    @Transactional(readOnly = true)
    public Page<DeliveryResult> searchDeliveries(UUID orderId, UUID sourceHubId, UUID destinationHubId, DeliveryStatus status, int page, int size) {
        Page<Delivery> deliveries = deliveryRepository.searchDeliveries(OrderId.of(orderId), HubId.of(sourceHubId), HubId.of(destinationHubId), status, page, size);
        return deliveries.map(DeliveryResult::from);
    }

    @Transactional
    public DeliveryResult updateRecipientInfo(UUID deliveryId, UpdateDeliveryRecipientInfoCommand command) {
        Delivery delivery = getDeliveryById(deliveryId);

        delivery.updateRecipientInfo(command.recipientInfo());

        return DeliveryResult.from(delivery);
    }

    @Transactional
    public DeliveryResult updateDeliveryStatus(UUID deliveryId, UpdateDeliveryStatusCommand command) {
        Delivery delivery = getDeliveryById(deliveryId);

        delivery.updateStatus(command.status());

        return DeliveryResult.from(delivery);
    }

    @Transactional
    public void deleteDelivery(UUID deliveryId) {
        Delivery delivery = getDeliveryById(deliveryId);

        // TODO: 배송 삭제자 기록 필요
        delivery.delete(UUID.randomUUID());
    }

    private Delivery getDeliveryById(UUID deliveryId) {
        return deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new DeliveryApplicationException(DeliveryApplicationErrorCode.NOT_FOUND_DELIVERY));
    }

    private void validateDuplicateOrderId(OrderId orderId) {
        if (deliveryRepository.existsByOrderId(orderId)) {
            throw new DeliveryApplicationException(DeliveryApplicationErrorCode.DUPLICATE_ORDER_ID);
        }
    }

    private void validateHubExists(HubId hubId) {
        if (!hubClient.existsById(hubId.getId())) {
            throw new DeliveryApplicationException(DeliveryApplicationErrorCode.NON_EXISTENT_HUB);
        }
    }
}
