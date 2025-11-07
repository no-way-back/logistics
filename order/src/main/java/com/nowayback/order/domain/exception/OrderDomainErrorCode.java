package com.nowayback.order.domain.exception;

import com.nowayback.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum OrderDomainErrorCode implements ErrorCode {

    NULL_SUPPLIER_COMPANY_ID("ORDER1001", "공급업체 ID는 null일 수 없습니다.", HttpStatus.BAD_REQUEST),
    NULL_SUPPLIER_COMPANY("ORDER1002", "공급업체는 null일 수 없습니다.", HttpStatus.BAD_REQUEST),
    NULL_RECEIVER_COMPANY_ID("ORDER1003", "수령업체 ID 객체는 null일 수 없습니다.", HttpStatus.BAD_REQUEST),
    NULL_RECEIVER_COMPANY("ORDER1004", "수령업체는 null일 수 없습니다.", HttpStatus.BAD_REQUEST),
    INVALID_ORDER_STATUS_TRANSITION("ORDER1005", "유효하지 않은 주문 상태 전환입니다.", HttpStatus.BAD_REQUEST),
    INVALID_ORDER_STATUS_FOR_DELETE("ORDER1006", "배송이 진행 중인 경우에는 주문을 삭제할 수 없습니다.",
        HttpStatus.BAD_REQUEST),
    INVALID_ORDER_ITEM_QUANTITY("ORDER1007", "주문 수량은 1개 이상이어 합니다.", HttpStatus.BAD_REQUEST),
    NULL_PRODUCT_ID("ORDER1008", "주문 상품 ID는 null일 수 없습니다.", HttpStatus.BAD_REQUEST),
    MISSING_ORDER_ITEM_NAME("ORDER1009", "주문 상품 이름은 null이거나 비어 있을 수 없습니다.", HttpStatus.BAD_REQUEST),
    INVALID_ORDER_ITEM_PRICE("ORDER1010", "상품 가격은 0보다 커야 합니다.", HttpStatus.BAD_REQUEST),
    ;

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    OrderDomainErrorCode(String code, String message, HttpStatus httpStatus) {
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
