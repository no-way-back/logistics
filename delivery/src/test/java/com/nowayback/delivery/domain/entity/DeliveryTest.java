package com.nowayback.delivery.domain.entity;

import com.nowayback.delivery.domain.delivery.entity.Delivery;
import com.nowayback.delivery.domain.delivery.exception.InvalidHubRouteException;
import com.nowayback.delivery.domain.delivery.vo.*;
import com.nowayback.delivery.domain.exception.InvalidObjectException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
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
}