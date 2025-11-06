package com.nowayback.delivery.domain.exception;

import exception.ErrorCode;
import exception.GlobalException;

public class InvalidHubRouteException extends GlobalException {

    public InvalidHubRouteException(ErrorCode errorCode) {
        super(errorCode);
    }
}
