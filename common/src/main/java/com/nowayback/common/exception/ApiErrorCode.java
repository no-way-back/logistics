package com.nowayback.common.exception;

import org.springframework.http.HttpStatus;

public enum ApiErrorCode implements ErrorCode {

    INVALID_REQUEST("API_400", "Invalid request parameters", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED("API_401", "Unauthorized access", HttpStatus.UNAUTHORIZED),
    FORBIDDEN("API_403", "Forbidden access", HttpStatus.FORBIDDEN),
    NOT_FOUND("API_404", "Resource not found", HttpStatus.NOT_FOUND),
    INTERNAL_SERVER_ERROR("API_500", "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus status;

    ApiErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
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
        return status;
    }
}
