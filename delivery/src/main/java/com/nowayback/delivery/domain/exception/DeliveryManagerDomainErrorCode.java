package com.nowayback.delivery.domain.exception;

import com.nowayback.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum DeliveryManagerDomainErrorCode implements ErrorCode {
    NULL_USER_ID_VALUE("DELIVERYMGR1001", "사용자 ID는 null일 수 없습니다.", HttpStatus.BAD_REQUEST),
    NULL_HUB_ID_VALUE("DELIVERYMGR1002", "허브 ID는 null일 수 없습니다.", HttpStatus.BAD_REQUEST),
    NEGATIVE_DELIVERY_SEQUENCE_VALUE("DELIVERYMGR1003", "배송 시퀀스 값은 음수일 수 없습니다.", HttpStatus.BAD_REQUEST),
    NULL_SLACK_ID_VALUE("DELIVERYMGR1004", "슬랙 ID는 null이거나 공백일 수 없습니다.", HttpStatus.BAD_REQUEST),

    NULL_HUB_ID_OBJECT("DELIVERYMGR1005", "허브 ID 객체는 null일 수 없습니다.", HttpStatus.BAD_REQUEST),
    NULL_DELIVERY_MANAGER_TYPE_OBJECT("DELIVERYMGR1006", "배송 관리자 타입 객체는 null일 수 없습니다.", HttpStatus.BAD_REQUEST),
    NULL_DELIVERY_SEQUENCE_OBJECT("DELIVERYMGR1007", "배송 시퀀스 객체는 null일 수 없습니다.", HttpStatus.BAD_REQUEST),
    NULL_DELIVERY_STATUS_OBJECT("DELIVERYMGR1008", "배송 상태 객체는 null일 수 없습니다.", HttpStatus.BAD_REQUEST),
    ;

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    DeliveryManagerDomainErrorCode(String code, String message, HttpStatus httpStatus) {
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
