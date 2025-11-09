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

import java.util.Optional;
import java.util.UUID;

import static com.nowayback.delivery.fixture.DeliveryFixture.*;
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
            CreateDeliveryCommand command = CREATE_DELIVERY_COMMAND;
            Delivery delivery = createDelivery();

            UUID sourceHubId = command.sourceHubId();
            UUID destinationHubId = command.destinationHubId();

            when(deliveryRepository.existsByOrderId(any(OrderId.class))).thenReturn(false);
            when(hubClient.existsById(sourceHubId)).thenReturn(true);
            when(hubClient.existsById(destinationHubId)).thenReturn(true);
            when(deliveryRepository.save(any(Delivery.class))).thenReturn(delivery);

            /* when */
            DeliveryResult result = deliveryService.createDelivery(command);

            /* then */
            assertThat(result.orderId()).isEqualTo(ORDER_UUID);
            assertThat(result.status()).isEqualTo(DeliveryStatus.WAITING_AT_HUB);
            assertThat(result.sourceHubId()).isEqualTo(sourceHubId);
            assertThat(result.destinationHubId()).isEqualTo(destinationHubId);
            assertThat(result.deliveryAddress()).isEqualTo(DELIVERY_ADDRESS);
            assertThat(result.recipientName()).isEqualTo(RECIPIENT_NAME);
            assertThat(result.recipientSlackId()).isEqualTo(RECIPIENT_SLACK_ID);
            assertThat(result.companyDeliveryManagerId()).isEqualTo(COMPANY_DELIVERY_MANAGER_UUID);

            verify(deliveryRepository, times(1)).existsByOrderId(any(OrderId.class));
            verify(hubClient, times(1)).existsById(sourceHubId);
            verify(hubClient, times(1)).existsById(destinationHubId);
            verify(deliveryRepository).save(any(Delivery.class));
        }

        @Test
        @DisplayName("중복된 주문 ID로 배송을 생성하면 예외가 발생한다.")
        void createDelivery_WithDuplicateOrderId_throwsException() {
            /* given */
            when(deliveryRepository.existsByOrderId(any(OrderId.class))).thenReturn(true);

            /* when */
            /* then */
            assertThatThrownBy(() -> {
                deliveryService.createDelivery(CREATE_DELIVERY_COMMAND);
            }).isInstanceOf(DeliveryApplicationException.class);
        }

        @Test
        @DisplayName("존재하지 않는 출발 허브 ID로 배송을 생성하면 예외가 발생한다.")
        void createDelivery_WithNonExistentSourceHubId_throwsException() {
            /* given */
            CreateDeliveryCommand command = CREATE_DELIVERY_COMMAND;
            UUID sourceHubId = command.sourceHubId();

            when(deliveryRepository.existsByOrderId(any(OrderId.class))).thenReturn(false);
            when(hubClient.existsById(sourceHubId)).thenReturn(false);

            /* when */
            /* then */
            assertThatThrownBy(() -> {
                deliveryService.createDelivery(command);
            }).isInstanceOf(DeliveryApplicationException.class);
        }


        @Test
        @DisplayName("존재하지 않는 도착 허브 ID로 배송을 생성하면 예외가 발생한다.")
        void createDelivery_WithNonExistentDestinationHubId_throwsException() {
            /* given */
            CreateDeliveryCommand command = CREATE_DELIVERY_COMMAND;
            UUID sourceHubId = command.sourceHubId();
            UUID destinationHubId = command.destinationHubId();

            when(deliveryRepository.existsByOrderId(any(OrderId.class))).thenReturn(false);
            when(hubClient.existsById(sourceHubId)).thenReturn(true);
            when(hubClient.existsById(destinationHubId)).thenReturn(false);

            /* when */
            /* then */
            assertThatThrownBy(() -> {
                deliveryService.createDelivery(command);
            }).isInstanceOf(DeliveryApplicationException.class);
        }
    }

    @Nested
    @DisplayName("배송 단일 조회")
    class GetDelivery {

        @Test
        @DisplayName("유효한 ID로 배송을 조회하면 해당 배송 정보가 반환된다.")
        void getDelivery_success() {
            /* given */
            Delivery delivery = createDelivery();

            when(deliveryRepository.findById(DELIVERY_UUID)).thenReturn(Optional.of(delivery));

            /* when */
            DeliveryResult result = deliveryService.getDelivery(DELIVERY_UUID);

            /* then */
            assertThat(result.orderId()).isEqualTo(ORDER_UUID);
            assertThat(result.status()).isEqualTo(DeliveryStatus.WAITING_AT_HUB);
            assertThat(result.sourceHubId()).isEqualTo(SOURCE_HUB_UUID);
            assertThat(result.destinationHubId()).isEqualTo(DESTINATION_HUB_UUID);
            assertThat(result.deliveryAddress()).isEqualTo(DELIVERY_ADDRESS);
            assertThat(result.recipientName()).isEqualTo(RECIPIENT_NAME);
            assertThat(result.recipientSlackId()).isEqualTo(RECIPIENT_SLACK_ID);
            assertThat(result.companyDeliveryManagerId()).isEqualTo(COMPANY_DELIVERY_MANAGER_UUID);
        }
    }
}