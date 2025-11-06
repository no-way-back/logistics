package com.nowayback.delivery.domain.delivery.exception;

import exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum DeliveryErrorCode implements ErrorCode {

    INVALID_HUB_ROUTE("DELIVERY400", "출발 허브와 도착 허브는 같을 수 없습니다.", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    DeliveryErrorCode(String code, String message, HttpStatus httpStatus) {
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
