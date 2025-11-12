package com.nowayback.product.domain.stock.exception;

import com.nowayback.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum StockDomainErrorCode implements ErrorCode {
    NULL_PRODUCT_ID_VALUE("STOCK1001", "상품 ID는 null일 수 없습니다.", HttpStatus.BAD_REQUEST),
    NEGATIVE_STOCK_QUANTITY("STOCK1002", "재고 수량은 음수일 수 없습니다.", HttpStatus.BAD_REQUEST),
    INSUFFICIENT_STOCK_QUANTITY("STOCK1003", "재고 수량이 부족합니다.", HttpStatus.BAD_REQUEST),

    NULL_PRODUCT_ID_OBJECT("STOCK1004", "상품 ID 객체는 null일 수 없습니다.", HttpStatus.BAD_REQUEST),
    NULL_QUANTITY_OBJECT("STOCK1005", "재고 수량 객체는 null일 수 없습니다.", HttpStatus.BAD_REQUEST)
    ;

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    StockDomainErrorCode(String code, String message, HttpStatus httpStatus) {
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
