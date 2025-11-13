package com.nowayback.delivery.application.exception;

import com.nowayback.common.exception.GlobalException;

public class DeliveryManagerApplicationException extends GlobalException {

    public DeliveryManagerApplicationException(DeliveryManagerApplicationErrorCode errorCode) {
        super(errorCode);
    }
}
