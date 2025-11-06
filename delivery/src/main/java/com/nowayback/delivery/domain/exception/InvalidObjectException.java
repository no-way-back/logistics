package com.nowayback.delivery.domain.exception;

import exception.ErrorCode;
import exception.GlobalException;

public class InvalidObjectException extends GlobalException {

    public InvalidObjectException(ErrorCode errorCode) {
        super(errorCode);
    }
}
