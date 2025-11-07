package com.nowayback.delivery.domain.exception;

import exception.ErrorCode;
import exception.GlobalException;

public class DeliveryDomainException extends GlobalException {

    public DeliveryDomainException(ErrorCode errorCode) {
        super(errorCode);
    }
}
