package com.nowayback.product.domain.product.exception;

import com.nowayback.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum ProductDomainErrorCode implements ErrorCode {

    NULL_COMPANY_ID_VALUE("PRODUCT1001", "업체 ID는 null일 수 없습니다.", HttpStatus.BAD_REQUEST),
    NULL_HUB_ID_VALUE("PRODUCT1002", "허브 ID는 null일 수 없습니다.", HttpStatus.BAD_REQUEST),
    INVALID_PRODUCT_NAME("PRODUCT1003", "상품 이름이 유효하지 않습니다.", HttpStatus.BAD_REQUEST),
    NEGATIVE_PRODUCT_PRICE("PRODUCT1004", "상품 가격은 0 이상이어야 합니다.", HttpStatus.BAD_REQUEST),

    NULL_SUPPLIER_ID_OBJECT("PRODUCT1005", "공급업체 ID 객체는 null일 수 없습니다.", HttpStatus.BAD_REQUEST),
    NULL_HUB_ID_OBJECT("PRODUCT1006", "허브 ID 객체는 null일 수 없습니다.", HttpStatus.BAD_REQUEST),
    NULL_PRODUCT_INFO_OBJECT("PRODUCT1007", "상품 정보는 null일 수 없습니다.", HttpStatus.BAD_REQUEST)
    ;

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    ProductDomainErrorCode(String code, String message, HttpStatus httpStatus) {
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
