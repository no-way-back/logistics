package com.nowayback.delivery.domain.exception;

import exception.ErrorCode;
import exception.GlobalException;

public class InvalidValueException extends GlobalException {

    public InvalidValueException(ErrorCode errorCode) {
        super(errorCode);
    }
}
