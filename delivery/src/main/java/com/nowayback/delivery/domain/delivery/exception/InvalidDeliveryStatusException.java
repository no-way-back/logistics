package com.nowayback.delivery.domain.delivery.exception;

import exception.ErrorCode;
import exception.GlobalException;

public class InvalidDeliveryStatusException extends GlobalException {

    public InvalidDeliveryStatusException(ErrorCode errorCode) {
        super(errorCode);
    }
}
