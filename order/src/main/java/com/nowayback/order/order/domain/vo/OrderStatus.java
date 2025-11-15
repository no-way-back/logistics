package com.nowayback.order.order.domain.vo;

public enum OrderStatus {
    CREATE_PENDING("주문 생성 대기"),
    CREATED("배송 준비 중"),
    DELIVERING("주문 배송 중"),
    COMPLETED("주문 배송 완료"),
    CANCELED("주문 취소"),
    ;

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }


}
