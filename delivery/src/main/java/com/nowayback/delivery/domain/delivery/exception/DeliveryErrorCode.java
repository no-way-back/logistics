package com.nowayback.delivery.domain.delivery.exception;

import exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum DeliveryErrorCode implements ErrorCode {

    INVALID_HUB_ROUTE("DELIVERY400", "출발 허브와 도착 허브는 같을 수 없습니다.", HttpStatus.BAD_REQUEST),

    NULL_ORDER_ID_OBJECT("DELIVERY400", "주문 ID 객체는 null일 수 없습니다.", HttpStatus.BAD_REQUEST),
    NULL_SOURCE_HUB_ID_OBJECT("DELIVERY400", "허브 ID 객체는 null일 수 없습니다.", HttpStatus.BAD_REQUEST),
    NULL_DESTINATION_HUB_ID_OBJECT("DELIVERY400", "허브 ID 객체는 null일 수 없습니다.", HttpStatus.BAD_REQUEST),
    NULL_RECIPIENT_INFO_OBJECT("DELIVERY400", "수령인 정보는 null일 수 없습니다.", HttpStatus.BAD_REQUEST),

    NULL_ORDER_ID_VALUE("DELIVERY400", "주문 ID는 null일 수 없습니다.", HttpStatus.BAD_REQUEST),
    NULL_HUB_ID_VALUE("DELIVERY400", "허브 ID는 null일 수 없습니다.", HttpStatus.BAD_REQUEST),
    NULL_DELIVERY_MANAGER_ID_VALUE("DELIVERY400", "배송 관리자 ID는 null일 수 없습니다.", HttpStatus.BAD_REQUEST),

    /* 수령인 정보 관련 */
    INVALID_RECIPIENT_ADDRESS("DELIVERY400", "수령인 주소가 유효하지 않습니다.", HttpStatus.BAD_REQUEST),
    INVALID_RECIPIENT_NAME("DELIVERY400", "수령인 이름이 유효하지 않습니다.", HttpStatus.BAD_REQUEST),
    INVALID_RECIPIENT_SLACK_ID("DELIVERY400", "수령인 슬랙 ID가 유효하지 않습니다.", HttpStatus.BAD_REQUEST),
    ;

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
