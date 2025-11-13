package com.nowayback.delivery.application.exception;

import com.nowayback.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum DeliveryManagerApplicationErrorCode implements ErrorCode {
    DUPLICATE_DELIVERY_MANAGER_SEQUENCE("DELIVERYMGR2001", "이미 존재하는 배송 관리자 시퀀스입니다.", HttpStatus.CONFLICT),
    DUPLICATE_DELIVERY_MANAGER_ID("DELIVERYMGR2002", "이미 존재하는 배송 관리자 ID입니다.", HttpStatus.CONFLICT),
    NOT_FOUND_DELIVERY_MANAGER("DELIVERYMGR2003", "존재하지 않는 배송 관리자입니다.", HttpStatus.NOT_FOUND),
    ;

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    DeliveryManagerApplicationErrorCode(String code, String message, HttpStatus httpStatus) {
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
