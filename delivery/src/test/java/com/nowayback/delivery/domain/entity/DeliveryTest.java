package com.nowayback.delivery.domain.entity;

import com.nowayback.delivery.domain.vo.DeliveryStatus;
import com.nowayback.delivery.domain.vo.RecipientInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class DeliveryTest {

    @Test
    @DisplayName("배송 생성 시 상태는 WAITING_AT_HUB이다.")
    void createDelivery_ShouldHaveWaitingAtHubStatus() {
        /* given */
        UUID orderId = UUID.randomUUID();
        UUID sourceHubId = UUID.randomUUID();
        UUID destinationHubId = UUID.randomUUID();

        String deliveryAddress = "서울특별시 중구 다산로46길 17 119호";
        String recipientName = "홍길동";
        String recipientSlackId = "slack_1234";
        RecipientInfo recipientInfo = RecipientInfo.of(deliveryAddress, recipientName, recipientSlackId);

        UUID companyDeliveryManagerId = UUID.randomUUID();

        /* when */
        Delivery delivery = Delivery.create(orderId, sourceHubId, destinationHubId, recipientInfo, companyDeliveryManagerId);

        /* then */
        assertThat(delivery.getStatus()).isEqualTo(DeliveryStatus.WAITING_AT_HUB);
    }
}