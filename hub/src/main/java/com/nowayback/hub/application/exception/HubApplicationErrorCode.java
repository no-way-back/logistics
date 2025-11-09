package com.nowayback.hub.application.exception;

import com.nowayback.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum HubApplicationErrorCode implements ErrorCode {

    HUB_NAME_ALREADY_EXISTS_EXCEPTION("HUB2001","이미 존재하는 허브 이름입니다.", HttpStatus.CONFLICT),
    HUB_ADDRESS_ALREADY_EXISTS_EXCEPTION("HUB2002", "이미 존재하는 허브 주소입니다.", HttpStatus.CONFLICT),
    HUB_NOT_FOUND_EXCEPTION("HUB2003", "허브를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),;

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

    HubApplicationErrorCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}