package com.nowayback.delivery.domain.delivery.entity;

import com.nowayback.delivery.domain.delivery.vo.*;
import com.nowayback.delivery.domain.delivery.exception.DeliveryDomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.UUID;

import static com.nowayback.delivery.fixture.DeliveryFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("배송 엔티티")
class DeliveryTest {

    @Nested
    @DisplayName("배송 생성")
    class CreateDelivery {

        @Test
        @DisplayName("모든 필드가 정상일 경우 배송 생성에 성공한다.")
        void createDelivery_success () {
            /* given */
            OrderId orderId = ORDER_ID;
            HubId sourceHubId = SOURCE_HUB_ID;
            HubId destinationHubId = DESTINATION_HUB_ID;
            RecipientInfo recipientInfo = RECIPIENT_INFO;
            DeliveryManagerId companyDeliveryManagerId = COMPANY_DELIVERY_MANAGER_ID;

            /* when */
            Delivery delivery = Delivery.create(orderId, sourceHubId, destinationHubId, recipientInfo, companyDeliveryManagerId);

            /* then */
            assertThat(delivery.getOrderId()).isEqualTo(orderId);
            assertThat(delivery.getSourceHubId()).isEqualTo(sourceHubId);
            assertThat(delivery.getDestinationHubId()).isEqualTo(destinationHubId);
            assertThat(delivery.getRecipientInfo()).isEqualTo(recipientInfo);
            assertThat(delivery.getCompanyDeliveryManagerId()).isEqualTo(companyDeliveryManagerId);
            assertThat(delivery.getStatus()).isNotNull();
        }

        @Test
        @DisplayName("생성 시 상태는 WAITING_AT_HUB이다.")
        void createDelivery_ShouldHaveWaitingAtHubStatus() {
            /* given */
            /* when */
            Delivery delivery = Delivery.create(ORDER_ID, SOURCE_HUB_ID, DESTINATION_HUB_ID, RECIPIENT_INFO, COMPANY_DELIVERY_MANAGER_ID);

            /* then */
            assertThat(delivery.getStatus()).isEqualTo(DeliveryStatus.WAITING_AT_HUB);
        }

        @Test
        @DisplayName("주문 ID는 null일 수 없다.")
        void createDelivery_ShouldNotAllowNullOrderId() {
            /* given */
            /* when */
            /* then */
            assertThatThrownBy(() -> {
                Delivery.create(null, SOURCE_HUB_ID, DESTINATION_HUB_ID, RECIPIENT_INFO, COMPANY_DELIVERY_MANAGER_ID);
            }).isInstanceOf(DeliveryDomainException.class);
        }

        @Test
        @DisplayName("출발 허브 ID는 null일 수 없다.")
        void createDelivery_ShouldNotAllowNullSourceHubId() {
            /* given */
            /* when */
            /* then */
            assertThatThrownBy(() -> {
                Delivery.create(ORDER_ID, null, DESTINATION_HUB_ID, RECIPIENT_INFO, COMPANY_DELIVERY_MANAGER_ID);
            }).isInstanceOf(DeliveryDomainException.class);
        }

        @Test
        @DisplayName("도착 허브 ID는 null일 수 없다.")
        void createDelivery_ShouldNotAllowNullDestinationHubId() {
            /* given */
            /* when */
            /* then */
            assertThatThrownBy(() -> {
                Delivery.create(ORDER_ID, SOURCE_HUB_ID, null, RECIPIENT_INFO, COMPANY_DELIVERY_MANAGER_ID);
            }).isInstanceOf(DeliveryDomainException.class);
        }

        @Test
        @DisplayName("수령인 정보는 null일 수 없다.")
        void createDelivery_ShouldNotAllowNullRecipientInfo() {
            /* given */
            /* when */
            /* then */
            assertThatThrownBy(() -> {
                Delivery.create(ORDER_ID, SOURCE_HUB_ID, DESTINATION_HUB_ID, null, COMPANY_DELIVERY_MANAGER_ID);
            }).isInstanceOf(DeliveryDomainException.class);
        }

        @Test
        @DisplayName("업체 배송 담당자 ID는 null일 수 없다.")
        void createDelivery_ShouldNotAllowNullCompanyDeliveryManagerId() {
            /* given */
            /* when */
            /* then */
            assertThatThrownBy(() -> {
                Delivery.create(ORDER_ID, SOURCE_HUB_ID, DESTINATION_HUB_ID, RECIPIENT_INFO, null);
            }).isInstanceOf(DeliveryDomainException.class);
        }

        @Test
        @DisplayName("출발 허브와 도착 허브가 같으면 안된다.")
        void createDelivery_ShouldNotAllowSameSourceAndDestinationHub() {
            /* given */
            /* when */
            /* then */
            assertThrows(DeliveryDomainException.class, () -> {
                Delivery.create(ORDER_ID, HUB_ID, HUB_ID, RECIPIENT_INFO, COMPANY_DELIVERY_MANAGER_ID);
            });
        }
    }

    @Nested
    @DisplayName("배송 수령인 정보 수정")
    class UpdateRecipientInfo {

        @Test
        @DisplayName("정상적인 배송 수령인 정보 수정 시 성공한다.")
        void updateRecipientInfo_success () {
            /* given */
            Delivery delivery = createDelivery();
            RecipientInfo newRecipientInfo = MODIFIED_RECIPIENT_INFO;

            /* when */
            delivery.updateRecipientInfo(newRecipientInfo);

            /* then */
            assertThat(delivery.getRecipientInfo()).isEqualTo(newRecipientInfo);
        }

        @Test
        @DisplayName("수령인 정보는 null일 수 없다.")
        void updateRecipientInfo_ShouldNotAllowNullRecipientInfo() {
            /* given */
            Delivery delivery = createDelivery();

            /* when */
            /* then */
            assertThatThrownBy(() -> {
                delivery.updateRecipientInfo(null);
            }).isInstanceOf(DeliveryDomainException.class);
        }

        @ParameterizedTest
        @EnumSource(value = DeliveryStatus.class, names = {"OUT_FOR_DELIVERY", "DELIVERED"})
        @DisplayName("OUT_FOR_DELIVERY, DELIVERED 상태에서는 수정할 수 없다.")
        void updateRecipientInfo_ShouldNotAllowUpdateInFinalStatuses(DeliveryStatus status) throws Exception {
            /* given */
            Delivery delivery = createDeliveryWithStatus(status);

            /* when */
            /* then */
            assertThatThrownBy(() -> {
                delivery.updateRecipientInfo(MODIFIED_RECIPIENT_INFO);
            }).isInstanceOf(DeliveryDomainException.class);
        }
    }

    @Nested
    @DisplayName("배송 상태 수정")
    class UpdateDeliveryStatus {

        @Test
        @DisplayName("정상적인 배송 상태 수정 시 성공한다.")
        void updateDeliveryStatus_success() {
            /* given */
            Delivery delivery = createDelivery();
            DeliveryStatus newStatus = DeliveryStatus.TRANSIT_BETWEEN_HUBS;

            /* when */
            delivery.updateStatus(newStatus);

            /* then */
            assertThat(delivery.getStatus()).isEqualTo(newStatus);
        }

        @ParameterizedTest
        @CsvSource({
                "WAITING_AT_HUB,        TRANSIT_BETWEEN_HUBS",
                "TRANSIT_BETWEEN_HUBS,  AT_DESTINATION_HUB",
                "AT_DESTINATION_HUB,    OUT_FOR_DELIVERY",
                "OUT_FOR_DELIVERY,      DELIVERED"
        })
        @DisplayName("정의된 다음 상태로만 전이할 수 있다.")
        void updateDeliveryStatus_ShouldAllowValidTransitions(DeliveryStatus initialStatus, DeliveryStatus newStatus) throws Exception {
            /* given */
            Delivery delivery = createDeliveryWithStatus(initialStatus);

            /* when */
            delivery.updateStatus(newStatus);

            /* then */
            assertThat(delivery.getStatus()).isEqualTo(newStatus);
        }

        @Test
        @DisplayName("유효하지 않은 상태 전이인 경우 예외가 발생한다.")
        void updateDeliveryStatus_ShouldNotAllowInvalidTransitions() {
            /* given */
            Delivery delivery = createDelivery();
            DeliveryStatus newStatus = DeliveryStatus.DELIVERED;

            /* when */
            /* then */
            assertThatThrownBy(() -> {
                delivery.updateStatus(newStatus);
            }).isInstanceOf(DeliveryDomainException.class);
        }
    }

    @Nested
    @DisplayName("배송 삭제")
    class DeleteDelivery {

        @Test
        @DisplayName("삭제 시 소프트 삭제 처리된다.")
        void deleteDelivery_success() {
            /* given */
            Delivery delivery = createDelivery();
            UUID actorId = UUID.randomUUID();

            /* when */
            delivery.delete(actorId);

            /* then */
            assertThat(delivery.getDeletedAt()).isNotNull();
            assertThat(delivery.getDeletedBy()).isEqualTo(actorId);
        }

        @ParameterizedTest
        @EnumSource(value = DeliveryStatus.class, names = {"WAITING_AT_HUB", "DELIVERED"})
        @DisplayName("상태가 WAITING_AT_HUB, DELIVERED인 경우에만 가능하다.")
        void deleteDelivery_ShouldAllowOnlyInSpecificStatuses(DeliveryStatus status) throws Exception {
            /* given */
            Delivery delivery = createDeliveryWithStatus(status);

            UUID actorId = UUID.randomUUID();

            /* when */
            delivery.delete(actorId);

            /* then */
            assertThat(delivery.getDeletedAt()).isNotNull();
            assertThat(delivery.getDeletedBy()).isEqualTo(actorId);
        }

        @ParameterizedTest
        @EnumSource(value = DeliveryStatus.class, names = {"TRANSIT_BETWEEN_HUBS", "AT_DESTINATION_HUB", "OUT_FOR_DELIVERY"})
        @DisplayName("상태가 WAITING_AT_HUB, DELIVERED외 경우에 불가능하다.")
        void deleteDelivery_ShouldNotAllowInOtherStatuses(DeliveryStatus status) throws Exception {
            /* given */
            Delivery delivery = createDeliveryWithStatus(status);
            UUID actorId = UUID.randomUUID();

            /* when */
            /* then */
            assertThatThrownBy(() -> {
                delivery.delete(actorId);
            }).isInstanceOf(DeliveryDomainException.class);
        }
    }
}