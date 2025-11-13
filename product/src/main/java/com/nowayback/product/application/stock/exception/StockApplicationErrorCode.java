package com.nowayback.product.application.stock.exception;

import com.nowayback.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum StockApplicationErrorCode implements ErrorCode {
    NOT_FOUND_STOCK("STOCK2001", "상품에 대한 재고를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    DUPLICATE_PRODUCT_ID("STOCK2002", "해당 상품에 대한 재고가 이미 존재합니다.", HttpStatus.BAD_REQUEST),
    ;

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    StockApplicationErrorCode(String code, String message, HttpStatus httpStatus) {
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
