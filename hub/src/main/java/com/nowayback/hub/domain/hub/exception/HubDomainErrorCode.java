package com.nowayback.hub.domain.hub.exception;

import com.nowayback.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum HubDomainErrorCode implements ErrorCode {
    HUB_NAME_MISSING_EXCEPTION("HUB1001", "허브 이름은 필수 입력 항목입니다.", HttpStatus.BAD_REQUEST),
    HUB_ADDRESS_MISSING_EXCEPTION("HUB1002", "주소는 필수 입력 항목입니다.", HttpStatus.BAD_REQUEST),
    HUB_LATITUDE_MISSING_EXCEPTION("HUB1003", "위도(Latitude) 값은 필수 입력 항목입니다.", HttpStatus.BAD_REQUEST),
    HUB_LONGITUDE_MISSING_EXCEPTION("HUB1004", "경도(Longitude) 값은 필수 입력 항목입니다.", HttpStatus.BAD_REQUEST),
    HUB_LATITUDE_RANGE_EXCEPTION("HUB1005", "위도는 -90.0에서 90.0 사이의 값이어야 합니다.", HttpStatus.BAD_REQUEST),
    HUB_LONGITUDE_RANGE_EXCEPTION("HUB1006", "경도는 -180.0에서 180.0 사이의 값이어야 합니다.", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

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

    HubDomainErrorCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
