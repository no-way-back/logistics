package com.nowayback.order.domain.exception;

import exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum OrderErrorCode implements ErrorCode {

    NULL_SUPPLIER_COMPANY_ID("ORDER001", "공급업체 ID는 null일 수 없습니다.", HttpStatus.BAD_REQUEST),
    NULL_SUPPLIER_COMPANY("ORDER002", "공급업체는 null일 수 없습니다.", HttpStatus.BAD_REQUEST),
    NULL_RECEIVER_COMPANY_ID("ORDER003", "수령업체 ID 객체는 null일 수 없습니다.", HttpStatus.BAD_REQUEST),
    NULL_RECEIVER_COMPANY("ORDER004", "수령업체는 null일 수 없습니다.", HttpStatus.BAD_REQUEST)
    ;

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    OrderErrorCode(String code, String message, HttpStatus httpStatus) {
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
