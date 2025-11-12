package com.nowayback.delivery.domain.deliverymanager.vo;

public enum DeliveryManagerType {

    HUB("허브 배송 관리자"),
    COMPANY("업체 배송 관리자");

    private final String description;

    DeliveryManagerType(String description) {
        this.description = description;
    }
}
