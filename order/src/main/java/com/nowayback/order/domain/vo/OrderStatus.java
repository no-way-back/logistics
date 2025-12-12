package com.nowayback.order.domain.vo;

public enum OrderStatus {
    CREATE_PENDING("주문 생성 대기"),
    CREATED("배송 준비 중"),
    DELIVERING("주문 배송 중"),
    COMPLETED("주문 배송 완료"),
    CANCELED("주문 취소"),
    STOCK_DECREASE_FAILED("재고 차감 실패"),
    PAYMENT_FAILED("결제 실패")
    ;

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }
}
