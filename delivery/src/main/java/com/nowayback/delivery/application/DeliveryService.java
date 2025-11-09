package com.nowayback.delivery.application;

import com.nowayback.delivery.application.command.CreateDeliveryCommand;
import com.nowayback.delivery.application.dto.DeliveryResult;
import com.nowayback.delivery.application.service.HubClient;
import com.nowayback.delivery.domain.delivery.entity.Delivery;
import com.nowayback.delivery.domain.delivery.repository.DeliveryRepository;
import com.nowayback.delivery.domain.delivery.vo.DeliveryManagerId;
import com.nowayback.delivery.domain.delivery.vo.HubId;
import com.nowayback.delivery.domain.delivery.vo.OrderId;
import com.nowayback.delivery.domain.delivery.vo.RecipientInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final HubClient hubClient;

    @Transactional
    public DeliveryResult createDelivery(CreateDeliveryCommand command) {
        validateDuplicateOrderId(command.orderId());
        validateHubExists(command.sourceHubId());
        validateHubExists(command.destinationHubId());

        // TODO: Delivery Manager 할당 로직 추가 필요
        UUID companyDeliveryManagerId = UUID.randomUUID();

        Delivery delivery = Delivery.create(
                OrderId.of(command.orderId()),
                HubId.of(command.sourceHubId()),
                HubId.of(command.destinationHubId()),
                RecipientInfo.of(
                        command.deliveryAddress(),
                        command.recipientName(),
                        command.recipientSlackId()
                ),
                DeliveryManagerId.of(companyDeliveryManagerId)
        );

        Delivery savedDelivery = deliveryRepository.save(delivery);

        // TODO: 배송 경로 생성 로직 추가 필요

        return DeliveryResult.from(savedDelivery);
    }

    private void validateDuplicateOrderId(UUID orderId) {
        if (deliveryRepository.existsByOrderId(OrderId.of(orderId))) {
            throw new IllegalArgumentException("Order id " + orderId + " already exists");
        }
    }

    private void validateHubExists(UUID hubId) {
        if (!hubClient.existsById(hubId)) {
            throw new IllegalArgumentException("Hub id " + hubId + " does not exist");
        }
    }
}
