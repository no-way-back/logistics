package com.nowayback.delivery.domain.deliveryroute.vo;

import lombok.Getter;

@Getter
public enum DeliveryRouteStatus {

    WAITING_AT_HUB("허브 대기 중") {
        @Override
        public boolean canTransitionTo(DeliveryRouteStatus newStatus) {
            return newStatus == TRANSIT_BETWEEN_HUBS;
        }
    },
    TRANSIT_BETWEEN_HUBS("허브 이동 중") {
        @Override
        public boolean canTransitionTo(DeliveryRouteStatus newStatus) {
            return newStatus == AT_DESTINATION_HUB;
        }
    },
    AT_DESTINATION_HUB("목적지 허브 도착") {
        @Override
        public boolean canTransitionTo(DeliveryRouteStatus newStatus) {
            return false;
        }
    },
    ;

    private final String description;

    DeliveryRouteStatus(String description) {
        this.description = description;
    }

    public abstract boolean canTransitionTo(DeliveryRouteStatus newStatus);
}
