package com.nowayback.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nowayback.common.exception.ErrorCode;
import com.nowayback.common.exception.GlobalException;

import java.util.List;

public record ExceptionResponse(
        String code,
        String message,
        @JsonInclude(JsonInclude.Include.NON_NULL) List<ErrorField> errors
) {

    public static ExceptionResponse from(GlobalException globalException) {
        return new ExceptionResponse(globalException.getErrorCode().getCode(), globalException.getMessage(), null);
    }

    public static ExceptionResponse from(ErrorCode errorCode) {
        return new ExceptionResponse(errorCode.getCode(), errorCode.getMessage(), null);
    }

    public static ExceptionResponse of(ErrorCode errorCode, List<ErrorField> errors) {
        return new ExceptionResponse(errorCode.getCode(), errorCode.getMessage(), errors);
    }

    public record ErrorField(Object value, String message) {
    }
}