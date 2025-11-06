package com.nowayback.delivery.domain.delivery.vo;

import lombok.Getter;

@Getter
public enum DeliveryStatus {

    WAITING_AT_HUB("허브 대기 중"),
    TRANSIT_BETWEEN_HUBS("허브 이동 중"),
    AT_DESTINATION_HUB("목적지 허브 도착"),
    OUT_FOR_DELIVERY("업체 이동 중"),
    DELIVERED("배송 완료");

    private final String description;

    DeliveryStatus(String description) {
        this.description = description;
    }

    public boolean canUpdateRecipientInfo() {
        return this != OUT_FOR_DELIVERY && this != DELIVERED;
    }
}
