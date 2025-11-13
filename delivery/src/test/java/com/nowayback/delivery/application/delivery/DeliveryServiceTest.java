package com.nowayback.delivery.application.delivery;

import com.nowayback.common.exception.GlobalException;
import com.nowayback.delivery.application.delivery.command.CreateDeliveryCommand;
import com.nowayback.delivery.application.delivery.command.UpdateDeliveryRecipientInfoCommand;
import com.nowayback.delivery.application.delivery.command.UpdateDeliveryStatusCommand;
import com.nowayback.delivery.application.delivery.dto.DeliveryResult;
import com.nowayback.delivery.application.delivery.exception.DeliveryApplicationErrorCode;
import com.nowayback.delivery.application.delivery.exception.DeliveryApplicationException;
import com.nowayback.delivery.application.delivery.service.HubClient;
import com.nowayback.delivery.application.deliverymanager.DeliveryManagerService;
import com.nowayback.delivery.application.deliverymanager.exception.DeliveryManagerApplicationErrorCode;
import com.nowayback.delivery.application.deliverymanager.exception.DeliveryManagerApplicationException;
import com.nowayback.delivery.application.deliveryroute.DeliveryRouteService;
import com.nowayback.delivery.application.deliveryroute.command.CreateDeliveryRoutesCommand;
import com.nowayback.delivery.application.deliveryroute.exception.DeliveryRouteApplicationErrorCode;
import com.nowayback.delivery.application.deliveryroute.exception.DeliveryRouteApplicationException;
import com.nowayback.delivery.domain.delivery.entity.Delivery;
import com.nowayback.delivery.domain.delivery.repository.DeliveryRepository;
import com.nowayback.delivery.domain.delivery.vo.*;
import com.nowayback.delivery.domain.deliverymanager.vo.DeliveryManagerType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import java.util.Collections;
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
    private DeliveryRouteService deliveryRouteService;

    @Mock
    private DeliveryManagerService deliveryManagerService;

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

            UUID sourceHubId = command.sourceHubId().getId();
            UUID destinationHubId = command.destinationHubId().getId();

            Delivery savedDelivery = spy(delivery);
            when(savedDelivery.getId()).thenReturn(DELIVERY_UUID);

            when(deliveryRepository.existsByOrderId(any(OrderId.class))).thenReturn(false);
            when(hubClient.existsById(sourceHubId)).thenReturn(true);
            when(hubClient.existsById(destinationHubId)).thenReturn(true);
            when(hubClient.getHubRoutesInfo(sourceHubId, destinationHubId)).thenReturn(HUB_ROUTES_INFO);
            when(deliveryManagerService.assignDeliveryManager(DeliveryManagerType.COMPANY)).thenReturn(COMPANY_DELIVERY_MANAGER_UUID);
            when(deliveryRepository.save(any(Delivery.class))).thenReturn(savedDelivery);
            when(deliveryRouteService.createDeliveryRoutes(any())).thenReturn(Collections.emptyList());

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
            verify(deliveryRouteService).createDeliveryRoutes(any());
        }

        @Test
        @DisplayName("중복된 주문 ID로 배송을 생성하면 예외가 발생한다.")
        void createDelivery_WithDuplicateOrderId_throwsException() {
            /* given */
            when(deliveryRepository.existsByOrderId(any(OrderId.class))).thenReturn(true);

            /* when */
            /* then */
            assertThatThrownBy(() -> deliveryService.createDelivery(CREATE_DELIVERY_COMMAND))
                    .isInstanceOf(DeliveryApplicationException.class)
                    .hasFieldOrPropertyWithValue("errorCode", DeliveryApplicationErrorCode.DUPLICATE_ORDER_ID);
        }

        @Test
        @DisplayName("존재하지 않는 출발 허브 ID로 배송을 생성하면 예외가 발생한다.")
        void createDelivery_WithNonExistentSourceHubId_throwsException() {
            /* given */
            CreateDeliveryCommand command = CREATE_DELIVERY_COMMAND;
            UUID sourceHubId = command.sourceHubId().getId();

            when(deliveryRepository.existsByOrderId(any(OrderId.class))).thenReturn(false);
            when(hubClient.existsById(sourceHubId)).thenReturn(false);

            /* when */
            /* then */
            assertThatThrownBy(() -> deliveryService.createDelivery(command))
                    .isInstanceOf(DeliveryApplicationException.class)
                    .hasFieldOrPropertyWithValue("errorCode", DeliveryApplicationErrorCode.NON_EXISTENT_HUB);
        }


        @Test
        @DisplayName("존재하지 않는 도착 허브 ID로 배송을 생성하면 예외가 발생한다.")
        void createDelivery_WithNonExistentDestinationHubId_throwsException() {
            /* given */
            CreateDeliveryCommand command = CREATE_DELIVERY_COMMAND;
            UUID sourceHubId = command.sourceHubId().getId();
            UUID destinationHubId = command.destinationHubId().getId();

            when(deliveryRepository.existsByOrderId(any(OrderId.class))).thenReturn(false);
            when(hubClient.existsById(sourceHubId)).thenReturn(true);
            when(hubClient.existsById(destinationHubId)).thenReturn(false);

            /* when */
            /* then */
            assertThatThrownBy(() -> deliveryService.createDelivery(command))
                    .isInstanceOf(DeliveryApplicationException.class)
                    .hasFieldOrPropertyWithValue("errorCode", DeliveryApplicationErrorCode.NON_EXISTENT_HUB);
        }

        @Test
        @DisplayName("업체 배송 관리자 할당에 실패하면 예외가 발생한다.")
        void createDelivery_FailedToAssignDeliveryManager_throwsException() {
            /* given */
            CreateDeliveryCommand command = CREATE_DELIVERY_COMMAND;
            UUID sourceHubId = command.sourceHubId().getId();
            UUID destinationHubId = command.destinationHubId().getId();

            when(deliveryRepository.existsByOrderId(any(OrderId.class))).thenReturn(false);
            when(hubClient.existsById(sourceHubId)).thenReturn(true);
            when(hubClient.existsById(destinationHubId)).thenReturn(true);
            when(deliveryManagerService.assignDeliveryManager(DeliveryManagerType.COMPANY))
                    .thenThrow(new DeliveryManagerApplicationException(DeliveryManagerApplicationErrorCode.NOT_FOUND_DELIVERY_MANAGER));

            /* when */
            /* then */
            assertThatThrownBy(() -> deliveryService.createDelivery(command))
                    .isInstanceOf(DeliveryApplicationException.class)
                    .hasFieldOrPropertyWithValue("errorCode", DeliveryApplicationErrorCode.FAILED_TO_ASSIGN_DELIVERY_MANAGER);
        }

        @Test
        @DisplayName("배송 경로 생성에 실패하면 예외가 발생한다.")
        void createDelivery_FailedToCreateDeliveryRoutes_throwsException() {
            /* given */
            CreateDeliveryCommand command = CREATE_DELIVERY_COMMAND;
            UUID sourceHubId = command.sourceHubId().getId();
            UUID destinationHubId = command.destinationHubId().getId();

            Delivery savedDelivery = spy(createDelivery());
            when(savedDelivery.getId()).thenReturn(DELIVERY_UUID);

            when(deliveryRepository.existsByOrderId(any(OrderId.class))).thenReturn(false);
            when(hubClient.existsById(sourceHubId)).thenReturn(true);
            when(hubClient.existsById(destinationHubId)).thenReturn(true);
            when(hubClient.getHubRoutesInfo(sourceHubId, destinationHubId)).thenReturn(HUB_ROUTES_INFO);
            when(deliveryManagerService.assignDeliveryManager(DeliveryManagerType.COMPANY)).thenReturn(COMPANY_DELIVERY_MANAGER_UUID);
            when(deliveryRepository.save(any(Delivery.class))).thenReturn(savedDelivery);
            when(deliveryRouteService.createDeliveryRoutes(any()))
                    .thenThrow(new DeliveryRouteApplicationException(DeliveryRouteApplicationErrorCode.DUPLICATE_ROUTE_SEQUENCE));

            /* when */
            /* then */
            assertThatThrownBy(() -> deliveryService.createDelivery(command))
                    .isInstanceOf(DeliveryApplicationException.class)
                    .hasFieldOrPropertyWithValue("errorCode", DeliveryApplicationErrorCode.FAILED_TO_CREATE_DELIVERY_ROUTES);
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

        @Test
        @DisplayName("존재하지 않는 ID로 배송을 조회하면 예외가 발생한다.")
        void getDelivery_WithNonExistentId_throwsException() {
            /* given */
            UUID deliveryId = DELIVERY_UUID;

            when(deliveryRepository.findById(deliveryId)).thenReturn(Optional.empty());

            /* when */
            /* then */
            assertThatThrownBy(() -> deliveryService.getDelivery(deliveryId))
                    .isInstanceOf(DeliveryApplicationException.class)
                    .hasFieldOrPropertyWithValue("errorCode", DeliveryApplicationErrorCode.NOT_FOUND_DELIVERY);
        }
    }

    @Nested
    @DisplayName("배송 검색 조회")
    class SearchDeliveries {

        @Test
        @DisplayName("검색 조건에 맞는 배송을 조회하면 해당 배송 리스트가 반환된다.")
        void searchDeliveries_success() {
            /* given */
            DeliveryStatus status = DELIVERY_STATUS;
            int page = PAGE;
            int size = SIZE;

            when(deliveryRepository.searchDeliveries(ORDER_ID, SOURCE_HUB_ID, DESTINATION_HUB_ID, status, page, size)).thenReturn(DELIVERY_PAGE);

            /* when */
            Page<DeliveryResult> result = deliveryService.searchDeliveries(ORDER_UUID, SOURCE_HUB_UUID, DESTINATION_HUB_UUID, status, page, size);

            /* then */
            assertThat(result.getTotalElements()).isEqualTo(DELIVERY_PAGE.getTotalElements());
            assertThat(result.getContent().size()).isEqualTo(DELIVERY_PAGE.getContent().size());
        }
    }

    @Nested
    @DisplayName("배송 수령인 정보 수정")
    class UpdateRecipientInfo {

        @Test
        @DisplayName("유효한 정보로 배송 수령인 정보를 수정하면 정보가 변경된다.")
        void updateRecipientInfo_success() {
            /* given */
            UpdateDeliveryRecipientInfoCommand command = UPDATE_DELIVERY_RECIPIENT_INFO_COMMAND;

            when(deliveryRepository.findById(DELIVERY_UUID)).thenReturn(Optional.of(createDelivery()));

            /* when */
            DeliveryResult result = deliveryService.updateRecipientInfo(DELIVERY_UUID, command);

            /* then */
            assertThat(result.deliveryAddress()).isEqualTo(DELIVERY_ADDRESS);
            assertThat(result.recipientName()).isEqualTo(MODIFIED_DELIVERY_NAME);
            assertThat(result.recipientSlackId()).isEqualTo(MODIFIED_DELIVERY_SLACK_ID);
        }

        @Test
        @DisplayName("존재하지 않는 ID로 배송 수령인 정보를 수정하면 예외가 발생한다.")
        void updateRecipientInfo_WithNonExistentId_throwsException() {
            /* given */
            when(deliveryRepository.findById(DELIVERY_UUID)).thenReturn(Optional.empty());

            /* when */
            /* then */
            assertThatThrownBy(() -> deliveryService.updateRecipientInfo(DELIVERY_UUID, UPDATE_DELIVERY_RECIPIENT_INFO_COMMAND))
                    .isInstanceOf(DeliveryApplicationException.class)
                    .hasFieldOrPropertyWithValue("errorCode", DeliveryApplicationErrorCode.NOT_FOUND_DELIVERY);
        }
    }

    @Nested
    @DisplayName("배송 상태 수정")
    class UpdateDeliveryStatus {

        @Test
        @DisplayName("유효한 정보로 배송 상태를 수정하면 상태가 변경된다.")
        void updateDeliveryStatus_success() {
            /* given */
            UpdateDeliveryStatusCommand command = UPDATE_DELIVERY_STATUS_COMMAND;

            when(deliveryRepository.findById(DELIVERY_UUID)).thenReturn(Optional.of(createDelivery()));

            /* when */
            DeliveryResult result = deliveryService.updateDeliveryStatus(DELIVERY_UUID, command);

            /* then */
            assertThat(result.status()).isEqualTo(command.status());
        }

        @Test
        @DisplayName("존재하지 않는 ID로 배송 상태를 수정하면 예외가 발생한다.")
        void updateDeliveryStatus_WithNonExistentId_throwsException() {
            /* given */
            when(deliveryRepository.findById(DELIVERY_UUID)).thenReturn(Optional.empty());

            /* when */
            /* then */
            assertThatThrownBy(() -> deliveryService.updateDeliveryStatus(DELIVERY_UUID, UPDATE_DELIVERY_STATUS_COMMAND))
                    .isInstanceOf(DeliveryApplicationException.class)
                    .hasFieldOrPropertyWithValue("errorCode", DeliveryApplicationErrorCode.NOT_FOUND_DELIVERY);
        }
    }

    @Nested
    @DisplayName("배송 삭제")
    class DeleteDelivery {

        @Test
        @DisplayName("유효한 ID로 배송을 삭제하면 배송이 삭제된다.")
        void deleteDelivery_success() {
            /* given */
            UUID actorId = UUID.randomUUID();
            when(deliveryRepository.findById(DELIVERY_UUID)).thenReturn(Optional.of(createDelivery()));

            /* when */
            deliveryService.deleteDelivery(actorId, DELIVERY_UUID);

            /* then */
            verify(deliveryRepository, times(1)).findById(DELIVERY_UUID);
            verify(deliveryRouteService, times(1)).deleteDeliveryRoutesByDeliveryId(actorId, DELIVERY_UUID);
        }

        @Test
        @DisplayName("존재하지 않는 ID로 배송을 삭제하면 예외가 발생한다.")
        void deleteDelivery_WithNonExistentId_throwsException() {
            /* given */
            UUID actorId = UUID.randomUUID();
            when(deliveryRepository.findById(DELIVERY_UUID)).thenReturn(Optional.empty());

            /* when */
            /* then */
            assertThatThrownBy(() -> deliveryService.deleteDelivery(actorId, DELIVERY_UUID))
                    .isInstanceOf(DeliveryApplicationException.class)
                    .hasFieldOrPropertyWithValue("errorCode", DeliveryApplicationErrorCode.NOT_FOUND_DELIVERY);
        }
    }
}