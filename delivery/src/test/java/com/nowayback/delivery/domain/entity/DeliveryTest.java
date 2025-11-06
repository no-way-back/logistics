package com.nowayback.delivery.domain.entity;

import com.nowayback.delivery.domain.delivery.entity.Delivery;
import com.nowayback.delivery.domain.delivery.exception.InvalidDeliveryStatusException;
import com.nowayback.delivery.domain.delivery.exception.InvalidHubRouteException;
import com.nowayback.delivery.domain.delivery.vo.*;
import com.nowayback.delivery.domain.exception.InvalidObjectException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import java.lang.reflect.Field;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class DeliveryTest {

    @Test
    @DisplayName("모든 필드가 정상일 경우 배송 생성에 성공한다.")
    void createDelivery_success () {
        /* given */
        OrderId orderId = OrderId.of(UUID.randomUUID());
        HubId sourceHubId = HubId.of(UUID.randomUUID());
        HubId destinationHubId = HubId.of(UUID.randomUUID());

        String deliveryAddress = "서울특별시 중구 다산로46길 17 119호";
        String recipientName = "홍길동";
        String recipientSlackId = "slack_1234";
        RecipientInfo recipientInfo = RecipientInfo.of(deliveryAddress, recipientName, recipientSlackId);

        DeliveryManagerId companyDeliveryManagerId = DeliveryManagerId.of(UUID.randomUUID());

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
    @DisplayName("배송 생성 시 상태는 WAITING_AT_HUB이다.")
    void createDelivery_ShouldHaveWaitingAtHubStatus() {
        /* given */
        OrderId orderId = OrderId.of(UUID.randomUUID());
        HubId sourceHubId = HubId.of(UUID.randomUUID());
        HubId destinationHubId = HubId.of(UUID.randomUUID());

        String deliveryAddress = "서울특별시 중구 다산로46길 17 119호";
        String recipientName = "홍길동";
        String recipientSlackId = "slack_1234";
        RecipientInfo recipientInfo = RecipientInfo.of(deliveryAddress, recipientName, recipientSlackId);

        DeliveryManagerId companyDeliveryManagerId = DeliveryManagerId.of(UUID.randomUUID());

        /* when */
        Delivery delivery = Delivery.create(orderId, sourceHubId, destinationHubId, recipientInfo, companyDeliveryManagerId);

        /* then */
        assertThat(delivery.getStatus()).isEqualTo(DeliveryStatus.WAITING_AT_HUB);
    }

    @Test
    @DisplayName("배송 생성 시 주문 ID는 null일 수 없다.")
    void createDelivery_ShouldNotAllowNullOrderId() {
        /* given */
        OrderId orderId = null;
        HubId sourceHubId = HubId.of(UUID.randomUUID());
        HubId destinationHubId = HubId.of(UUID.randomUUID());

        String deliveryAddress = "서울특별시 중구 다산로46길 17 119호";
        String recipientName = "홍길동";
        String recipientSlackId = "slack_1234";
        RecipientInfo recipientInfo = RecipientInfo.of(deliveryAddress, recipientName, recipientSlackId);

        DeliveryManagerId companyDeliveryManagerId = DeliveryManagerId.of(UUID.randomUUID());

        /* when */
        /* then */
        assertThatThrownBy(() -> {
            Delivery.create(orderId, sourceHubId, destinationHubId, recipientInfo, companyDeliveryManagerId);
        }).isInstanceOf(InvalidObjectException.class);
    }
    
    @Test
    @DisplayName("배송 생성 시 출발 허브 ID는 null일 수 없다.")
    void createDelivery_ShouldNotAllowNullSourceHubId() {
        /* given */
        OrderId orderId = OrderId.of(UUID.randomUUID());
        HubId sourceHubId = null;
        HubId destinationHubId = HubId.of(UUID.randomUUID());

        String deliveryAddress = "서울특별시 중구 다산로46길 17 119호";
        String recipientName = "홍길동";
        String recipientSlackId = "slack_1234";
        RecipientInfo recipientInfo = RecipientInfo.of(deliveryAddress, recipientName, recipientSlackId);

        DeliveryManagerId companyDeliveryManagerId = DeliveryManagerId.of(UUID.randomUUID());
        
        /* when */
        /* then */
        assertThatThrownBy(() -> {
            Delivery.create(orderId, sourceHubId, destinationHubId, recipientInfo, companyDeliveryManagerId);
        }).isInstanceOf(InvalidObjectException.class);
    }

    @Test
    @DisplayName("배송 생성 시 도착 허브 ID는 null일 수 없다.")
    void createDelivery_ShouldNotAllowNullDestinationHubId() {
        /* given */
        OrderId orderId = OrderId.of(UUID.randomUUID());
        HubId sourceHubId = HubId.of(UUID.randomUUID());
        HubId destinationHubId = null;

        String deliveryAddress = "서울특별시 중구 다산로46길 17 119호";
        String recipientName = "홍길동";
        String recipientSlackId = "slack_1234";
        RecipientInfo recipientInfo = RecipientInfo.of(deliveryAddress, recipientName, recipientSlackId);

        DeliveryManagerId companyDeliveryManagerId = DeliveryManagerId.of(UUID.randomUUID());

        /* when */
        /* then */
        assertThatThrownBy(() -> {
            Delivery.create(orderId, sourceHubId, destinationHubId, recipientInfo, companyDeliveryManagerId);
        }).isInstanceOf(InvalidObjectException.class);
    }

    @Test
    @DisplayName("배송 생성 시 수령인 정보는 null일 수 없다.")
    void createDelivery_ShouldNotAllowNullRecipientInfo() {
        /* given */
        OrderId orderId = OrderId.of(UUID.randomUUID());
        HubId sourceHubId = HubId.of(UUID.randomUUID());
        HubId destinationHubId = HubId.of(UUID.randomUUID());
        RecipientInfo recipientInfo = null;
        DeliveryManagerId companyDeliveryManagerId = DeliveryManagerId.of(UUID.randomUUID());

        /* when */
        /* then */
        assertThatThrownBy(() -> {
            Delivery.create(orderId, sourceHubId, destinationHubId, recipientInfo, companyDeliveryManagerId);
        }).isInstanceOf(InvalidObjectException.class);
    }

    @Test
    @DisplayName("배송 생성 시 업체 배송 담당자 ID는 null일 수 없다.")
    void createDelivery_ShouldNotAllowNullCompanyDeliveryManagerId() {
        /* given */
        OrderId orderId = OrderId.of(UUID.randomUUID());
        HubId sourceHubId = HubId.of(UUID.randomUUID());
        HubId destinationHubId = HubId.of(UUID.randomUUID());

        String deliveryAddress = "서울특별시 중구 다산로46길 17 119호";
        String recipientName = "홍길동";
        String recipientSlackId = "slack_1234";
        RecipientInfo recipientInfo = RecipientInfo.of(deliveryAddress, recipientName, recipientSlackId);

        DeliveryManagerId companyDeliveryManagerId = null;

        /* when */
        /* then */
        assertThatThrownBy(() -> {
            Delivery.create(orderId, sourceHubId, destinationHubId, recipientInfo, companyDeliveryManagerId);
        }).isInstanceOf(InvalidObjectException.class);
    }

    @Test
    @DisplayName("배송 생성 시 출발 허브와 도착 허브가 같으면 안된다.")
    void createDelivery_ShouldNotAllowSameSourceAndDestinationHub() {
        /* given */
        OrderId orderId = OrderId.of(UUID.randomUUID());
        HubId hubId = HubId.of(UUID.randomUUID());

        String deliveryAddress = "서울특별시 중구 다산로46길 17 119호";
        String recipientName = "홍길동";
        String recipientSlackId = "slack_1234";
        RecipientInfo recipientInfo = RecipientInfo.of(deliveryAddress, recipientName, recipientSlackId);

        DeliveryManagerId companyDeliveryManagerId = DeliveryManagerId.of(UUID.randomUUID());

        /* when */
        /* then */
        assertThrows(InvalidHubRouteException.class, () -> {
            Delivery.create(orderId, hubId, hubId, recipientInfo, companyDeliveryManagerId);
        });
    }

    @Test
    @DisplayName("정상적인 배송 수령인 정보 수정 시 성공한다.")
    void updateRecipientInfo_success () {
        /* given */
        OrderId orderId = OrderId.of(UUID.randomUUID());
        HubId sourceHubId = HubId.of(UUID.randomUUID());
        HubId destinationHubId = HubId.of(UUID.randomUUID());

        String deliveryAddress = "서울특별시 중구 다산로46길 17 119호";
        String recipientName = "홍길동";
        String recipientSlackId = "slack_1234";
        RecipientInfo recipientInfo = RecipientInfo.of(deliveryAddress, recipientName, recipientSlackId);

        String newRecipientName = "김철수";
        String newRecipientSlackId = "slack_5678";
        RecipientInfo newRecipientInfo = RecipientInfo.of(deliveryAddress, newRecipientName, newRecipientSlackId);

        DeliveryManagerId companyDeliveryManagerId = DeliveryManagerId.of(UUID.randomUUID());

        /* when */
        Delivery delivery = Delivery.create(orderId, sourceHubId, destinationHubId, recipientInfo, companyDeliveryManagerId);
        delivery.updateRecipientInfo(newRecipientInfo);

        /* then */
        assertThat(delivery.getRecipientInfo()).isEqualTo(newRecipientInfo);
    }

    @Test
    @DisplayName("배송 수령인 정보 수정 시 수령인 정보는 null일 수 없다.")
    void updateRecipientInfo_ShouldNotAllowNullRecipientInfo() {
        /* given */
        OrderId orderId = OrderId.of(UUID.randomUUID());
        HubId sourceHubId = HubId.of(UUID.randomUUID());
        HubId destinationHubId = HubId.of(UUID.randomUUID());

        String deliveryAddress = "서울특별시 중구 다산로46길 17 119호";
        String recipientName = "홍길동";
        String recipientSlackId = "slack_1234";
        RecipientInfo recipientInfo = RecipientInfo.of(deliveryAddress, recipientName, recipientSlackId);

        DeliveryManagerId companyDeliveryManagerId = DeliveryManagerId.of(UUID.randomUUID());

        /* when */
        Delivery delivery = Delivery.create(orderId, sourceHubId, destinationHubId, recipientInfo, companyDeliveryManagerId);

        /* then */
        assertThatThrownBy(() -> {
            delivery.updateRecipientInfo(null);
        }).isInstanceOf(InvalidObjectException.class);
    }

    @ParameterizedTest
    @EnumSource(value = DeliveryStatus.class, names = {"OUT_FOR_DELIVERY", "DELIVERED"})
    @DisplayName("배송 수령인 정보 수정 시 OUT_FOR_DELIVERY, DELIVERED 상태에서는 수정할 수 없다.")
    void updateRecipientInfo_ShouldNotAllowUpdateInFinalStatuses(DeliveryStatus status) throws Exception {
        /* given */
        OrderId orderId = OrderId.of(UUID.randomUUID());
        HubId sourceHubId = HubId.of(UUID.randomUUID());
        HubId destinationHubId = HubId.of(UUID.randomUUID());

        String deliveryAddress = "서울특별시 중구 다산로46길 17 119호";
        String recipientName = "홍길동";
        String recipientSlackId = "slack_1234";
        RecipientInfo recipientInfo = RecipientInfo.of(deliveryAddress, recipientName, recipientSlackId);

        String newRecipientName = "김철수";
        String newRecipientSlackId = "slack_5678";
        RecipientInfo newRecipientInfo = RecipientInfo.of(deliveryAddress, newRecipientName, newRecipientSlackId);

        DeliveryManagerId companyDeliveryManagerId = DeliveryManagerId.of(UUID.randomUUID());

        /* when */
        Delivery delivery = Delivery.create(orderId, sourceHubId, destinationHubId, recipientInfo, companyDeliveryManagerId);
        Field statusField = Delivery.class.getDeclaredField("status");
        statusField.setAccessible(true);
        statusField.set(delivery, status);

        /* then */
        assertThatThrownBy(() -> {
            delivery.updateRecipientInfo(newRecipientInfo);
        }).isInstanceOf(InvalidDeliveryStatusException.class);
    }

    @Test
    @DisplayName("정상적인 배송 상태 수정 시 성공한다.")
    void updateDeliveryStatus_success() {
        /* given */
        OrderId orderId = OrderId.of(UUID.randomUUID());
        HubId sourceHubId = HubId.of(UUID.randomUUID());
        HubId destinationHubId = HubId.of(UUID.randomUUID());

        String deliveryAddress = "서울특별시 중구 다산로46길 17 119호";
        String recipientName = "홍길동";
        String recipientSlackId = "slack_1234";
        RecipientInfo recipientInfo = RecipientInfo.of(deliveryAddress, recipientName, recipientSlackId);

        DeliveryManagerId companyDeliveryManagerId = DeliveryManagerId.of(UUID.randomUUID());

        DeliveryStatus newStatus = DeliveryStatus.TRANSIT_BETWEEN_HUBS;

        /* when */
        Delivery delivery = Delivery.create(orderId, sourceHubId, destinationHubId, recipientInfo, companyDeliveryManagerId);
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
    @DisplayName("배송 상태 변경 시 다음 상태로만 전이할 수 있다.")
    void updateDeliveryStatus_ShouldAllowValidTransitions(DeliveryStatus initialStatus, DeliveryStatus newStatus) throws Exception {
        /* given */
        OrderId orderId = OrderId.of(UUID.randomUUID());
        HubId sourceHubId = HubId.of(UUID.randomUUID());
        HubId destinationHubId = HubId.of(UUID.randomUUID());

        String deliveryAddress = "서울특별시 중구 다산로46길 17 119호";
        String recipientName = "홍길동";
        String recipientSlackId = "slack_1234";
        RecipientInfo recipientInfo = RecipientInfo.of(deliveryAddress, recipientName, recipientSlackId);

        DeliveryManagerId companyDeliveryManagerId = DeliveryManagerId.of(UUID.randomUUID());

        /* when */
        Delivery delivery = Delivery.create(orderId, sourceHubId, destinationHubId, recipientInfo, companyDeliveryManagerId);
        Field statusField = Delivery.class.getDeclaredField("status");
        statusField.setAccessible(true);
        statusField.set(delivery, initialStatus);

        delivery.updateStatus(newStatus);

        /* then */
        assertThat(delivery.getStatus()).isEqualTo(newStatus);
    }

    @Test
    @DisplayName("배송 상태 변경 시 유효하지 않은 상태 전이인 경우 예외가 발생한다.")
    void updateDeliveryStatus_ShouldNotAllowInvalidTransitions() {
        /* given */
        OrderId orderId = OrderId.of(UUID.randomUUID());
        HubId sourceHubId = HubId.of(UUID.randomUUID());
        HubId destinationHubId = HubId.of(UUID.randomUUID());

        String deliveryAddress = "서울특별시 중구 다산로46길 17 119호";
        String recipientName = "홍길동";
        String recipientSlackId = "slack_1234";
        RecipientInfo recipientInfo = RecipientInfo.of(deliveryAddress, recipientName, recipientSlackId);

        DeliveryManagerId companyDeliveryManagerId = DeliveryManagerId.of(UUID.randomUUID());

        DeliveryStatus newStatus = DeliveryStatus.DELIVERED;

        /* when */
        Delivery delivery = Delivery.create(orderId, sourceHubId, destinationHubId, recipientInfo, companyDeliveryManagerId);

        /* then */
        assertThatThrownBy(() -> {
            delivery.updateStatus(newStatus);
        }).isInstanceOf(InvalidDeliveryStatusException.class);
    }
}