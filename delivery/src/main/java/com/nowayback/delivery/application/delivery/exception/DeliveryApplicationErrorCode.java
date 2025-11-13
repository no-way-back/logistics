package com.nowayback.delivery.application.delivery.exception;

import com.nowayback.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum DeliveryApplicationErrorCode implements ErrorCode {

    DUPLICATE_ORDER_ID("DELIVERY2001", "해당 주문에 대한 배송이 이미 존재합니다.", HttpStatus.CONFLICT),
    NON_EXISTENT_HUB("DELIVERY2002", "해당 허브가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    NOT_FOUND_DELIVERY("DELIVERY2003", "해당 배송을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    FAILED_TO_ASSIGN_DELIVERY_MANAGER("DELIVERY2004", "배송 관리자 할당에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    FAILED_TO_CREATE_DELIVERY_ROUTES("DELIVERY2005", "배송 경로 생성에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    ;

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    DeliveryApplicationErrorCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public HttpStatus getStatus() {
        return httpStatus;
    }
}
