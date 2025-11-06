package com.nowayback.delivery.domain.delivery.vo;

import lombok.Getter;

@Getter
public enum DeliveryStatus {

    WAITING_AT_HUB("허브 대기 중") {
        @Override
        public boolean canTransitionTo(DeliveryStatus newStatus) {
            return newStatus == TRANSIT_BETWEEN_HUBS;
        }
    },
    TRANSIT_BETWEEN_HUBS("허브 이동 중") {
        @Override
        public boolean canTransitionTo(DeliveryStatus newStatus) {
            return newStatus == AT_DESTINATION_HUB;
        }
    },
    AT_DESTINATION_HUB("목적지 허브 도착") {
        @Override
        public boolean canTransitionTo(DeliveryStatus newStatus) {
            return newStatus == OUT_FOR_DELIVERY;
        }
    },
    OUT_FOR_DELIVERY("업체 이동 중") {
        @Override
        public boolean canTransitionTo(DeliveryStatus newStatus) {
            return newStatus == DELIVERED;
        }
    },
    DELIVERED("배송 완료") {
        @Override
        public boolean canTransitionTo(DeliveryStatus newStatus) {
            return false;
        }
    };

    private final String description;

    DeliveryStatus(String description) {
        this.description = description;
    }

    public abstract boolean canTransitionTo(DeliveryStatus newStatus);

    public boolean canUpdateRecipientInfo() {
        return this != OUT_FOR_DELIVERY && this != DELIVERED;
    }
}
