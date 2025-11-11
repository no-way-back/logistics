package com.nowayback.delivery.domain.exception;

import com.nowayback.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum DeliveryRouteDomainErrorCode implements ErrorCode {

    NULL_DELIVERY_ID_VALUE("DELIVERYROUTE1001", "배송 ID는 null일 수 없습니다.", HttpStatus.BAD_REQUEST),
    NULL_DELIVERY_MANAGER_ID_VALUE("DELIVERYROUTE1002", "배송 관리자 ID는 null일 수 없습니다.", HttpStatus.BAD_REQUEST),
    NULL_HUB_ID_VALUE("DELIVERYROUTE1003", "허브 ID는 null일 수 없습니다.", HttpStatus.BAD_REQUEST),
    NULL_SOURCE_HUB_ID("DELIVERYROUTE1004", "출발 허브 ID는 null일 수 없습니다.", HttpStatus.BAD_REQUEST),
    NULL_DESTINATION_HUB_ID("DELIVERYROUTE1005", "도착 허브 ID는 null일 수 없습니다.", HttpStatus.BAD_REQUEST),

    INVALID_HUB_ROUTE_SAME_HUB("DELIVERYROUTE1006", "출발 허브와 도착 허브는 같을 수 없습니다.", HttpStatus.BAD_REQUEST),
    INVALID_DISTANCE_METERS("DELIVERYROUTE1007", "거리(미터) 값은 0 이상이어야 합니다.", HttpStatus.BAD_REQUEST),
    INVALID_DURATION_MINUTES("DELIVERYROUTE1008", "소요 시간(분) 값은 0 이상이어야 합니다.", HttpStatus.BAD_REQUEST),
    INVALID_ROUTE_SEQUENCE("DELIVERYROUTE1009", "경로 순서 값은 0 이상이어야 합니다.", HttpStatus.BAD_REQUEST),

    NULL_DELIVERY_ID_OBJECT("DELIVERYROUTE1010", "배송 ID 객체는 null일 수 없습니다.", HttpStatus.BAD_REQUEST),
    NULL_ROUTE_SEQUENCE_OBJECT("DELIVERYROUTE1011", "경로 순서 객체는 null일 수 없습니다.", HttpStatus.BAD_REQUEST),
    NULL_HUB_ROUTE_OBJECT("DELIVERYROUTE1012", "허브 경로 객체는 null일 수 없습니다.", HttpStatus.BAD_REQUEST),
    NULL_DELIVERY_MANAGER_ID_OBJECT("DELIVERYROUTE1013", "배송 관리자 ID 객체는 null일 수 없습니다.", HttpStatus.BAD_REQUEST),
    NULL_ROUTE_INFO_OBJECT("DELIVERYROUTE1014", "경로 정보 객체는 null일 수 없습니다.", HttpStatus.BAD_REQUEST),

    INVALID_DELIVERY_ROUTE_STATUS_TRANSITION("DELIVERYROUTE1015", "유효하지 않은 배송 경로 상태 전환입니다.", HttpStatus.BAD_REQUEST),
    INVALID_DELIVERY_ROUTE_STATUS_FOR_UPDATE("DELIVERYROUTE1016", "현재 배송 경로 상태에서는 경로 정보를 수정할 수 없습니다.", HttpStatus.BAD_REQUEST),
    ;

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    DeliveryRouteDomainErrorCode(String code, String message, HttpStatus httpStatus) {
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
