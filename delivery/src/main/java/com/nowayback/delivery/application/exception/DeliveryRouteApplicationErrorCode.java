package com.nowayback.delivery.application.exception;

import com.nowayback.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum DeliveryRouteApplicationErrorCode implements ErrorCode {

    NOT_FOUND_DELIVERY_ROUTE("DELIVERYROUTE2001", "해당 배송 경로를 찾을 수 없습니다.", HttpStatus.NOT_FOUND)

    ;

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    DeliveryRouteApplicationErrorCode(String code, String message, HttpStatus httpStatus) {
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
