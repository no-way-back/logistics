package com.nowayback.delivery.domain.deliveryroute.entity;

import com.nowayback.delivery.domain.deliveryroute.vo.*;
import com.nowayback.delivery.domain.deliveryroute.exception.DeliveryRouteDomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.UUID;

import static com.nowayback.delivery.fixture.DeliveryRouteFixture.*;
import static org.assertj.core.api.Assertions.*;

@DisplayName("배송 경로 엔티티")
class DeliveryRouteTest {

    @Nested
    @DisplayName("배송 경로 생성")
    class CreateDeliveryRoute {

        @Test
        @DisplayName("모든 필드가 정상일 경우 배송 경로 생성에 성공한다.")
        void createDeliveryRoute_Success() {
            /* given */
            DeliveryId deliveryId = DELIVERY_ID;
            RouteSequence routeSequence = ROUTE_SEQUENCE;
            HubRoute hubRoute = HUB_ROUTE;
            DeliveryManagerId deliveryManagerId = DELIVERY_MANAGER_ID;
            RouteInfo routeInfo = ROUTE_INFO;

            /* when */
            DeliveryRoute route = DeliveryRoute.create(deliveryId, routeSequence, hubRoute, deliveryManagerId, routeInfo);

            /* then */
            assertThat(route.getDeliveryId()).isEqualTo(deliveryId);
            assertThat(route.getSequence()).isEqualTo(routeSequence);
            assertThat(route.getHubRoute()).isEqualTo(hubRoute);
            assertThat(route.getDeliveryManagerId()).isEqualTo(deliveryManagerId);
            assertThat(route.getRouteInfo()).isEqualTo(routeInfo);
            assertThat(route.getStatus()).isNotNull();
        }

        @Test
        @DisplayName("생성 시 상태는 WAITING_AT_HUB이다.")
        void createDeliveryRoute_WhenCreate_ShouldHaveWaitingAtHubStatus() {
            /* given */
            /* when */
            DeliveryRoute route = DeliveryRoute.create(DELIVERY_ID, ROUTE_SEQUENCE, HUB_ROUTE, DELIVERY_MANAGER_ID, ROUTE_INFO);

            /* then */
            assertThat(route.getStatus()).isEqualTo(DeliveryRouteStatus.WAITING_AT_HUB);
        }

        @Test
        @DisplayName("배송 ID는 null일 수 없다.")
        void createDeliveryRoute_WhenDeliveryIdIsNull_ShouldThrowException() {
            /* given */
            /* when */
            /* then */
            assertThatThrownBy(() -> {
                DeliveryRoute.create(null, ROUTE_SEQUENCE, HUB_ROUTE, DELIVERY_MANAGER_ID, ROUTE_INFO);
            }).isInstanceOf(DeliveryRouteDomainException.class);
        }

        @Test
        @DisplayName("경로 순서는 null일 수 없다.")
        void createDeliveryRoute_WhenRouteSequenceIsNull_ShouldThrowException() {
            /* given */
            /* when */
            /* then */
            assertThatThrownBy(() -> {
                DeliveryRoute.create(DELIVERY_ID, null, HUB_ROUTE, DELIVERY_MANAGER_ID, ROUTE_INFO);
            }).isInstanceOf(DeliveryRouteDomainException.class);
        }

        @Test
        @DisplayName("허브 경로는 null일 수 없다.")
        void createDeliveryRoute_WhenHubRouteIsNull_ShouldThrowException() {
            /* given */
            /* when */
            /* then */
            assertThatThrownBy(() -> {
                DeliveryRoute.create(DELIVERY_ID, ROUTE_SEQUENCE, null, DELIVERY_MANAGER_ID, ROUTE_INFO);
            }).isInstanceOf(DeliveryRouteDomainException.class);
        }

        @Test
        @DisplayName("배송 담당자 ID는 null일 수 없다.")
        void createDeliveryRoute_WhenDeliveryManagerIdIsNull_ShouldThrowException() {
            /* given */
            /* when */
            /* then */
            assertThatThrownBy(() -> {
                DeliveryRoute.create(DELIVERY_ID, ROUTE_SEQUENCE, HUB_ROUTE, null, ROUTE_INFO);
            }).isInstanceOf(DeliveryRouteDomainException.class);
        }

        @Test
        @DisplayName("경로 정보는 null일 수 없다.")
        void createDeliveryRoute_WhenRouteInfoIsNull_ShouldThrowException() {
            /* given */
            /* when */
            /* then */
            assertThatThrownBy(() -> {
                DeliveryRoute.create(DELIVERY_ID, ROUTE_SEQUENCE, HUB_ROUTE, DELIVERY_MANAGER_ID, null);
            }).isInstanceOf(DeliveryRouteDomainException.class);
        }
    }

    @Nested
    @DisplayName("배송 경로 상태 수정")
    class UpdateDeliveryRouteStatus {

        @Test
        @DisplayName("정상적인 배송 경로 상태 수정 시 성공한다.")
        void updateDeliveryRouteStatus_Success() {
            /* given */
            DeliveryRoute route = createDeliveryRoute();
            DeliveryRouteStatus newStatus = DeliveryRouteStatus.TRANSIT_BETWEEN_HUBS;

            /* when */
            route.updateStatus(newStatus);

            /* then */
            assertThat(route.getStatus()).isEqualTo(newStatus);
        }

        @ParameterizedTest
        @CsvSource({
                "WAITING_AT_HUB,        TRANSIT_BETWEEN_HUBS",
                "TRANSIT_BETWEEN_HUBS,  AT_DESTINATION_HUB",
        })
        @DisplayName("정의된 다음 상태로만 전이할 수 있다.")
        void updateDeliveryRouteStatus_ValidTransitions_Success(DeliveryRouteStatus initialStatus, DeliveryRouteStatus newStatus) {
            /* given */
            DeliveryRoute route = createDeliveryRoute(initialStatus);

            /* when */
            route.updateStatus(newStatus);

            /* then */
            assertThat(route.getStatus()).isEqualTo(newStatus);
        }

        @Test
        @DisplayName("유효하지 않은 상태 전이인 경우 예외가 발생한다.")
        void updateDeliveryRouteStatus_InvalidTransition_ShouldThrowException() {
            /* given */
            DeliveryRoute route = createDeliveryRoute();
            DeliveryRouteStatus newStatus = DeliveryRouteStatus.AT_DESTINATION_HUB;

            /* when */
            /* then */
            assertThatThrownBy(() -> {
                route.updateStatus(newStatus);
            }).isInstanceOf(DeliveryRouteDomainException.class);
        }
    }

    @Nested
    @DisplayName("배송 경로 정보 수정")
    class UpdateDeliveryRouteInfo {

        @Test
        @DisplayName("정상적인 배송 경로 정보 수정 시 성공한다.")
        void updateDeliveryRouteInfo_Success() {
            /* given */
            DeliveryRoute route = createDeliveryRoute(DeliveryRouteStatus.AT_DESTINATION_HUB);
            RouteInfo newRouteInfo = MODIFIED_ROUTE_INFO;

            /* when */
            route.updateRouteInfo(newRouteInfo);

            /* then */
            assertThat(route.getRouteInfo()).isEqualTo(newRouteInfo);
        }

        @Test
        @DisplayName("배송 경로 정보는 null일 수 없다.")
        void updateDeliveryRouteInfo_WhenRouteInfoIsNull_ShouldThrowException() {
            /* given */
            DeliveryRoute route = createDeliveryRoute();

            /* when */
            /* then */
            assertThatThrownBy(() -> {
                route.updateRouteInfo(null);
            }).isInstanceOf(DeliveryRouteDomainException.class);
        }

        @Test
        @DisplayName("AT_DESTINATION_HUB 상태에서만 경로 정보 수정이 가능하다.")
        void updateDeliveryRouteInfo_WhenAtDestinationHub_ShouldSucceed() {
            /* given */
            DeliveryRoute route = createDeliveryRoute(DeliveryRouteStatus.AT_DESTINATION_HUB);

            /* when */
            route.updateRouteInfo(MODIFIED_ROUTE_INFO);

            /* then */
            assertThat(route.getRouteInfo()).isEqualTo(MODIFIED_ROUTE_INFO);
        }

        @ParameterizedTest
        @EnumSource(value = DeliveryRouteStatus.class, names = {"WAITING_AT_HUB", "TRANSIT_BETWEEN_HUBS"})
        @DisplayName("AT_DESTINATION_HUB 외의 상태에서는 경로 정보 수정이 불가능하다.")
        void updateDeliveryRouteInfo_WhenNotAtDestinationHub_ShouldThrowException(DeliveryRouteStatus status) {
            /* given */
            DeliveryRoute route = createDeliveryRoute(status);

            /* when */
            /* then */
            assertThatThrownBy(() -> {
                route.updateRouteInfo(MODIFIED_ROUTE_INFO);
            }).isInstanceOf(DeliveryRouteDomainException.class);
        }
    }

    @Nested
    @DisplayName("배송 경로 삭제")
    class DeleteDeliveryRoute {

        @Test
        @DisplayName("삭제 시 소프트 삭제 처리된다.")
        void deleteDeliveryRoute_Success() {
            /* given */
            DeliveryRoute route = createDeliveryRoute();
            UUID actorId = UUID.randomUUID();

            /* when */
            route.delete(actorId);

            /* then */
            assertThat(route.getDeletedAt()).isNotNull();
            assertThat(route.getDeletedBy()).isEqualTo(actorId);
        }
    }
}