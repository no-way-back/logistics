package com.nowayback.order.order.application.exception;

import com.nowayback.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum OrderApplicationErrorCode implements ErrorCode {
    NOT_ENOUGH_STOCK("ORDER2001", "주문 상품의 재고가 충분하지 않습니다.", HttpStatus.BAD_REQUEST),
    DELIVERY_CREATION_FAILED("ORDER2002", "배송 생성에 실패했습니다.", HttpStatus.FAILED_DEPENDENCY),
    ;

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    OrderApplicationErrorCode(String code, String message, HttpStatus httpStatus) {
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
