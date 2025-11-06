package com.nowayback.delivery.domain.delivery.vo;

import lombok.Getter;

@Getter
public enum DeliveryStatus {

    WAITING_AT_HUB("허브 대기 중") {
        @Override
        public boolean canUpdateRecipientInfo() {
            return true;
        }
    },
    TRANSIT_BETWEEN_HUBS("허브 이동 중") {
        @Override
        public boolean canUpdateRecipientInfo() {
            return true;
        }
    },
    AT_DESTINATION_HUB("목적지 허브 도착") {
        @Override
        public boolean canUpdateRecipientInfo() {
            return true;
        }
    },
    OUT_FOR_DELIVERY("업체 이동 중") {
        @Override
        public boolean canUpdateRecipientInfo() {
            return false;
        }
    },
    DELIVERED("배송 완료") {
        @Override
        public boolean canUpdateRecipientInfo() {
            return false;
        }
    };

    private final String description;

    DeliveryStatus(String description) {
        this.description = description;
    }

    public abstract boolean canUpdateRecipientInfo();
}
