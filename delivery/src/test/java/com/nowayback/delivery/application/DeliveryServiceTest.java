package com.nowayback.delivery.application;

import com.nowayback.delivery.application.command.CreateDeliveryCommand;
import com.nowayback.delivery.application.dto.DeliveryResult;
import com.nowayback.delivery.application.exception.DeliveryApplicationException;
import com.nowayback.delivery.application.service.HubClient;
import com.nowayback.delivery.domain.delivery.entity.Delivery;
import com.nowayback.delivery.domain.delivery.repository.DeliveryRepository;
import com.nowayback.delivery.domain.delivery.vo.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeliveryServiceTest {

    @Mock
    private DeliveryRepository deliveryRepository;

    @Mock
    private HubClient hubClient;

    @InjectMocks
    private DeliveryService deliveryService;

    @Nested
    @DisplayName("배송 생성")
    class CreateDelivery {

        @Test
        @DisplayName("유효한 정보로 배송을 생성하면 WAITING_AT_HUB 상태의 배송이 저장된다.")
        void createDelivery_success() {
            /* given */
            UUID orderId = UUID.randomUUID();
            UUID sourceHubId = UUID.randomUUID();
            UUID destinationHubId = UUID.randomUUID();
            String deliveryAddress = "서울특별시 중구 다산로46길 17 119호";
            String recipientName = "홍길동";
            String recipientSlackId = "slack_1234";
            UUID companyDeliveryManagerId = UUID.randomUUID();

            CreateDeliveryCommand command = new CreateDeliveryCommand(
                    orderId,
                    sourceHubId,
                    destinationHubId,
                    deliveryAddress,
                    recipientName,
                    recipientSlackId
            );


            Delivery delivery = Delivery.create(
                    OrderId.of(orderId),
                    HubId.of(sourceHubId),
                    HubId.of(destinationHubId),
                    RecipientInfo.of(deliveryAddress, recipientName, recipientSlackId),
                    DeliveryManagerId.of(companyDeliveryManagerId)
            );

            when(deliveryRepository.existsByOrderId(any(OrderId.class))).thenReturn(false);
            when(hubClient.existsById(sourceHubId)).thenReturn(true);
            when(hubClient.existsById(destinationHubId)).thenReturn(true);
            when(deliveryRepository.save(any(Delivery.class))).thenReturn(delivery);

            /* when */
            DeliveryResult result = deliveryService.createDelivery(command);

            /* then */
            assertThat(result.orderId()).isEqualTo(orderId);
            assertThat(result.status()).isEqualTo(DeliveryStatus.WAITING_AT_HUB);
            assertThat(result.sourceHubId()).isEqualTo(sourceHubId);
            assertThat(result.destinationHubId()).isEqualTo(destinationHubId);
            assertThat(result.deliveryAddress()).isEqualTo(deliveryAddress);
            assertThat(result.recipientName()).isEqualTo(recipientName);
            assertThat(result.recipientSlackId()).isEqualTo(recipientSlackId);
            assertThat(result.companyDeliveryManagerId()).isEqualTo(companyDeliveryManagerId);

            verify(deliveryRepository, times(1)).existsByOrderId(any(OrderId.class));
            verify(hubClient, times(1)).existsById(sourceHubId);
            verify(hubClient, times(1)).existsById(destinationHubId);
            verify(deliveryRepository).save(any(Delivery.class));
        }

        @Test
        @DisplayName("중복된 주문 ID로 배송을 생성하면 예외가 발생한다.")
        void createDelivery_WithDuplicateOrderId_throwsException() {
            /* given */
            UUID orderId = UUID.randomUUID();
            UUID sourceHubId = UUID.randomUUID();
            UUID destinationHubId = UUID.randomUUID();
            String deliveryAddress = "서울특별시 중구 다산로46길 17 119호";
            String recipientName = "홍길동";
            String recipientSlackId = "slack_1234";
            UUID companyDeliveryManagerId = UUID.randomUUID();

            CreateDeliveryCommand command = new CreateDeliveryCommand(
                    orderId,
                    sourceHubId,
                    destinationHubId,
                    deliveryAddress,
                    recipientName,
                    recipientSlackId
            );

            when(deliveryRepository.existsByOrderId(any(OrderId.class))).thenReturn(true);

            /* when */
            /* then */
            assertThatThrownBy(() -> {
                deliveryService.createDelivery(command);
            }).isInstanceOf(DeliveryApplicationException.class);
        }
    }
}