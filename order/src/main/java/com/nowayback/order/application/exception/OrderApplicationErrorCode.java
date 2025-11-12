package com.nowayback.order.application.exception;

import com.nowayback.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum OrderApplicationErrorCode implements ErrorCode {
    NOT_ENOUGH_STOCK("ORDER2001", "주문 상품의 재고가 충분하지 않습니다.", HttpStatus.BAD_REQUEST),
    DELIVERY_CREATION_FAILED("ORDER2002", "배송 생성에 실패했습니다.", HttpStatus.FAILED_DEPENDENCY),
    ORDER_NOT_FOUND("ORDER2003", "해당 주문이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    UNAUTHORIZED_ORDER_ACCESS("ORDER2004", "해당 주문에 권한이 없습니다.", HttpStatus.FORBIDDEN),
    STOCK_RESTORE_FAILED("ORDER2005", "상품 재고 복구에 실패했습니다.", HttpStatus.FAILED_DEPENDENCY);

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
