package com.nowayback.order.domain.vo;

public enum OrderStatus {
    CREATE_PENDING("주문 생성 대기") {
        @Override
        public boolean canTransitionTo(OrderStatus newStatus) {
            return newStatus == CREATED;
        }
    },
    CREATED("주문 생성") {
        @Override
        public boolean canTransitionTo(OrderStatus newStatus) {
            return newStatus == DELIVERING;
        }
    },
    DELIVERING("주문 배송 중") {
        @Override
        public boolean canTransitionTo(OrderStatus newStatus) {
            return newStatus == COMPLETED;
        }
    },
    COMPLETED("주문 배송 완료") {
        @Override
        public boolean canTransitionTo(OrderStatus newStatus) {
            return false;
        }
    },
    CANCELED("주문 취소") {
        @Override
        public boolean canTransitionTo(OrderStatus newStatus) {
            return false;
        }
    }
    ;

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public abstract boolean canTransitionTo(OrderStatus orderStatus);

    public boolean canBeDeleted() {
        return this == CREATED || this == CREATE_PENDING;
    }
}
