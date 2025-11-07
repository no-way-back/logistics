package com.nowayback.delivery.domain.exception;

import com.nowayback.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum DeliveryDomainErrorCode implements ErrorCode {

    INVALID_HUB_ROUTE("DELIVERY1001", "출발 허브와 도착 허브는 같을 수 없습니다.", HttpStatus.BAD_REQUEST),
    INVALID_DELIVERY_STATUS_FOR_UPDATE("DELIVERY1002", "배송이 진행 중이거나 완료된 경우에는 배송 정보를 변경할 수 없습니다.", HttpStatus.BAD_REQUEST),
    INVALID_DELIVERY_STATUS_TRANSITION("DELIVERY1003", "유효하지 않은 배송 상태 전환입니다.", HttpStatus.BAD_REQUEST),
    INVALID_DELIVERY_STATUS_FOR_DELETE("DELIVERY1004", "배송이 진행 중인 경우에는 배송을 삭제할 수 없습니다.", HttpStatus.BAD_REQUEST),

    NULL_ORDER_ID_OBJECT("DELIVERY1005", "주문 ID 객체는 null일 수 없습니다.", HttpStatus.BAD_REQUEST),
    NULL_SOURCE_HUB_ID_OBJECT("DELIVERY1006", "허브 ID 객체는 null일 수 없습니다.", HttpStatus.BAD_REQUEST),
    NULL_DESTINATION_HUB_ID_OBJECT("DELIVERY1007", "허브 ID 객체는 null일 수 없습니다.", HttpStatus.BAD_REQUEST),
    NULL_RECIPIENT_INFO_OBJECT("DELIVERY1008", "수령인 정보는 null일 수 없습니다.", HttpStatus.BAD_REQUEST),
    NULL_DELIVERY_MANAGER_ID_OBJECT("DELIVERY1009", "배송 관리자 ID 객체는 null일 수 없습니다.", HttpStatus.BAD_REQUEST),

    NULL_ORDER_ID_VALUE("DELIVERY1010", "주문 ID는 null일 수 없습니다.", HttpStatus.BAD_REQUEST),
    NULL_HUB_ID_VALUE("DELIVERY1011", "허브 ID는 null일 수 없습니다.", HttpStatus.BAD_REQUEST),
    NULL_DELIVERY_MANAGER_ID_VALUE("DELIVERY1012", "배송 관리자 ID는 null일 수 없습니다.", HttpStatus.BAD_REQUEST),

    /* 수령인 정보 관련 */
    INVALID_RECIPIENT_ADDRESS("DELIVERY1013", "수령인 주소가 유효하지 않습니다.", HttpStatus.BAD_REQUEST),
    INVALID_RECIPIENT_NAME("DELIVERY1014", "수령인 이름이 유효하지 않습니다.", HttpStatus.BAD_REQUEST),
    INVALID_RECIPIENT_SLACK_ID("DELIVERY1015", "수령인 슬랙 ID가 유효하지 않습니다.", HttpStatus.BAD_REQUEST),
    ;

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    DeliveryDomainErrorCode(String code, String message, HttpStatus httpStatus) {
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
