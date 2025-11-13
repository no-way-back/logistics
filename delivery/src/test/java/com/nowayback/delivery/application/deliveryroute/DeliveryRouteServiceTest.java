package com.nowayback.delivery.application.deliveryroute;

import com.nowayback.delivery.application.deliveryroute.command.CreateDeliveryRoutesCommand;
import com.nowayback.delivery.application.deliveryroute.command.UpdateDeliveryRouteInfoCommand;
import com.nowayback.delivery.application.deliveryroute.command.UpdateDeliveryRouteStatusCommand;
import com.nowayback.delivery.application.deliveryroute.dto.DeliveryRouteResult;
import com.nowayback.delivery.application.deliveryroute.exception.DeliveryRouteApplicationErrorCode;
import com.nowayback.delivery.application.deliveryroute.exception.DeliveryRouteApplicationException;
import com.nowayback.delivery.domain.deliveryroute.entity.DeliveryRoute;
import com.nowayback.delivery.domain.deliveryroute.repository.DeliveryRouteRepository;
import com.nowayback.delivery.domain.deliveryroute.vo.DeliveryId;
import com.nowayback.delivery.domain.deliveryroute.vo.DeliveryRouteStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.nowayback.delivery.fixture.DeliveryRouteFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("배송 경로 서비스")
class DeliveryRouteServiceTest {

    @Mock
    private DeliveryRouteRepository deliveryRouteRepository;

    @InjectMocks
    private DeliveryRouteService deliveryRouteService;

    @Nested
    @DisplayName("배송 경로 생성")
    class CreateDeliveryRoutes {

        @Test
        @DisplayName("유효한 정보로 배송 경로를 생성하면 성공한다.")
        void createDeliveryRoutes_Success() {
            /* given */
            CreateDeliveryRoutesCommand command = CREATE_DELIVERY_ROUTES_COMMAND;
            DeliveryRoute route = createDeliveryRoute();

            when(deliveryRouteRepository.saveAll(anyList()))
                    .thenReturn(List.of(route));

            /* when */
            List<DeliveryRouteResult> results = deliveryRouteService.createDeliveryRoutes(command);

            /* then */
            assertThat(results.size()).isEqualTo(1);
            assertThat(results.get(0).sequence()).isEqualTo(SEQUENCE);
            assertThat(results.get(0).sourceHubId()).isEqualTo(SOURCE_HUB_UUID);
            assertThat(results.get(0).destinationHubId()).isEqualTo(DESTINATION_HUB_UUID);
            assertThat(results.get(0).hubDeliveryManagerId()).isEqualTo(DELIVERY_MANAGER_UUID);
            assertThat(results.get(0).status()).isEqualTo(DeliveryRouteStatus.WAITING_AT_HUB);
            assertThat(results.get(0).expectedDistanceMeters()).isEqualTo(EXPECTED_DISTANCE_METERS);
            assertThat(results.get(0).expectedDurationMinutes()).isEqualTo(EXPECTED_DURATION_MINUTES);
            assertThat(results.get(0).actualDistanceMeters()).isNull();
            assertThat(results.get(0).actualDurationMinutes()).isNull();

            verify(deliveryRouteRepository).saveAll(anyList());
        }

        @Test
        @DisplayName("중복된 순번이 있을 경우 예외가 발생한다.")
        void createDeliveryRoutes_DuplicateSequence_ShouldThrowException() {
            /* given */
            CreateDeliveryRoutesCommand command = CREATE_DELIVERY_ROUTES_COMMAND_WITH_DUPLICATE_SEQUENCE;

            /* when */
            /* then */
            assertThatThrownBy(() -> deliveryRouteService.createDeliveryRoutes(command))
                    .isInstanceOf(DeliveryRouteApplicationException.class)
                    .hasFieldOrPropertyWithValue("errorCode", DeliveryRouteApplicationErrorCode.DUPLICATE_ROUTE_SEQUENCE);
        }
    }

    @Nested
    @DisplayName("배송 경로 단일 조회")
    class GetDeliveryRoute {

        @Test
        @DisplayName("유효한 배송 경로 ID로 조회하면 해당 배송 경로 정보가 반환된다.")
        void getDeliveryRoute_Success() {
            /* given */
            DeliveryRoute route = createDeliveryRoute();
            UUID deliveryRouteId = DELIVERY_ROUTE_UUID;

            when(deliveryRouteRepository.findById(deliveryRouteId)).thenReturn(Optional.of(route));

            /* when */
            DeliveryRouteResult result = deliveryRouteService.getDelivery(USER_UUID, USER_ROLE, deliveryRouteId);

            /* then */
            assertThat(result.sequence()).isEqualTo(SEQUENCE);
            assertThat(result.sourceHubId()).isEqualTo(SOURCE_HUB_UUID);
            assertThat(result.destinationHubId()).isEqualTo(DESTINATION_HUB_UUID);
            assertThat(result.hubDeliveryManagerId()).isEqualTo(DELIVERY_MANAGER_UUID);
            assertThat(result.status()).isEqualTo(DeliveryRouteStatus.WAITING_AT_HUB);
            assertThat(result.expectedDistanceMeters()).isEqualTo(EXPECTED_DISTANCE_METERS);
            assertThat(result.expectedDurationMinutes()).isEqualTo(EXPECTED_DURATION_MINUTES);
            assertThat(result.actualDistanceMeters()).isNull();
            assertThat(result.actualDurationMinutes()).isNull();
        }

        @Test
        @DisplayName("존재하지 않는 배송 경로 ID로 조회하면 예외가 발생한다.")
        void getDeliveryRoute_WithNonExistentId_ShouldThrowException() {
            /* given */
            UUID deliveryRouteId = DELIVERY_ROUTE_UUID;

            when(deliveryRouteRepository.findById(deliveryRouteId)).thenReturn(Optional.empty());

            /* when */
            /* then */
            assertThatThrownBy(() -> deliveryRouteService.getDelivery(USER_UUID, USER_ROLE, deliveryRouteId))
                    .isInstanceOf(DeliveryRouteApplicationException.class)
                    .hasFieldOrPropertyWithValue("errorCode", DeliveryRouteApplicationErrorCode.NOT_FOUND_DELIVERY_ROUTE);
        }
    }

    @Nested
    @DisplayName("배송 경로 검색 조회")
    class SearchDeliveryRoutes {

        @Test
        @DisplayName("검색 조건에 맞는 배송 경로를 조회하면 배송 경로 페이지 목록이 반환된다.")
        void searchDeliveryRoutes_Success() {
            /* given */
            Pageable pageable = PageRequest.of(0, 10, Sort.by("sequence").ascending());

            when(deliveryRouteRepository.findAllByDeliveryId(DELIVERY_ID, pageable))
                    .thenReturn(DELIVERY_ROUTES_PAGE);

            /* when */
            Page<DeliveryRouteResult> results = deliveryRouteService
                    .searchDeliveryRoutes(USER_UUID, USER_ROLE, DELIVERY_UUID, pageable);

            /* then */
            assertThat(results.getTotalElements()).isEqualTo(DELIVERY_ROUTES.size());
            assertThat(results.getContent().get(0).sequence()).isEqualTo(SEQUENCE);

            verify(deliveryRouteRepository).findAllByDeliveryId(DELIVERY_ID, pageable);
        }
    }

    @Nested
    @DisplayName("배송 경로 상태 수정")
    class UpdateDeliveryRouteStatus {

        @Test
        @DisplayName("유효한 정보로 배송 경로 상태를 수정하면 성공한다.")
        void updateDeliveryRouteStatus_Success() {
            /* given */
            UpdateDeliveryRouteStatusCommand command = UPDATE_DELIVERY_ROUTE_STATUS_COMMAND;

            when(deliveryRouteRepository.findById(DELIVERY_ROUTE_UUID)).thenReturn(Optional.of(createDeliveryRoute()));

            /* when */
            DeliveryRouteResult result = deliveryRouteService.updateDeliveryRouteStatus(USER_UUID, USER_ROLE, DELIVERY_ROUTE_UUID, command);

            /* then */
            assertThat(result.status()).isEqualTo(command.status());

            verify(deliveryRouteRepository).findById(DELIVERY_ROUTE_UUID);
        }

        @Test
        @DisplayName("존재하지 않는 배송 경로 ID로 상태 수정 시도하면 예외가 발생한다.")
        void updateDeliveryRouteStatus_WithNonExistentId_ShouldThrowException() {
            /* given */
            when(deliveryRouteRepository.findById(DELIVERY_ROUTE_UUID)).thenReturn(Optional.empty());

            /* when */
            /* then */
            assertThatThrownBy(() -> deliveryRouteService.updateDeliveryRouteStatus(USER_UUID, USER_ROLE, DELIVERY_ROUTE_UUID, UPDATE_DELIVERY_ROUTE_STATUS_COMMAND))
                    .isInstanceOf(DeliveryRouteApplicationException.class)
                    .hasFieldOrPropertyWithValue("errorCode", DeliveryRouteApplicationErrorCode.NOT_FOUND_DELIVERY_ROUTE);
        }
    }

    @Nested
    @DisplayName("배송 경로 정보 수정")
    class UpdateDeliveryRouteInfo {

        @Test
        @DisplayName("유효한 정보로 배송 경로 정보를 수정하면 성공한다.")
        void updateDeliveryRouteInfo_Success() {
            /* given */
            UpdateDeliveryRouteInfoCommand command = UPDATE_DELIVERY_ROUTE_INFO_COMMAND;

            when(deliveryRouteRepository.findById(DELIVERY_ROUTE_UUID)).thenReturn(Optional.of(createDeliveryRoute(DeliveryRouteStatus.AT_DESTINATION_HUB)));

            /* when */
            DeliveryRouteResult result = deliveryRouteService.updateDeliveryRouteInfo(USER_UUID, USER_ROLE, DELIVERY_ROUTE_UUID, command);

            /* then */
            assertThat(result.actualDistanceMeters()).isEqualTo(command.actualDistanceMeters());
            assertThat(result.actualDurationMinutes()).isEqualTo(command.actualDurationMinutes());
        }

        @Test
        @DisplayName("존재하지 않는 배송 경로 ID로 정보 수정 시도하면 예외가 발생한다.")
        void updateDeliveryRouteInfo_WithNonExistentId_ShouldThrowException() {
            /* given */
            when(deliveryRouteRepository.findById(DELIVERY_ROUTE_UUID)).thenReturn(Optional.empty());

            /* when */
            /* then */
            assertThatThrownBy(() -> deliveryRouteService.updateDeliveryRouteInfo(USER_UUID, USER_ROLE, DELIVERY_ROUTE_UUID, UPDATE_DELIVERY_ROUTE_INFO_COMMAND))
                    .isInstanceOf(DeliveryRouteApplicationException.class)
                    .hasFieldOrPropertyWithValue("errorCode", DeliveryRouteApplicationErrorCode.NOT_FOUND_DELIVERY_ROUTE);
        }
    }

    @Test
    @DisplayName("배송 ID에 대한 배송 경로 삭제")
    void deleteByDeliveryId_Success() {
        /* given */
        UUID deliveryId = DELIVERY_UUID;

        when(deliveryRouteRepository.findAllByDeliveryId(DeliveryId.of(deliveryId)))
                .thenReturn(DELIVERY_ROUTES);

        /* when */
        deliveryRouteService.deleteDeliveryRoutesByDeliveryId(USER_UUID, deliveryId);

        /* then */
        verify(deliveryRouteRepository).findAllByDeliveryId(DeliveryId.of(deliveryId));
    }
}