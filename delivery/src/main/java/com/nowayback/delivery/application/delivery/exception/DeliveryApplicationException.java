package com.nowayback.delivery.application.delivery.exception;

import com.nowayback.common.exception.ErrorCode;
import com.nowayback.common.exception.GlobalException;

public class DeliveryApplicationException extends GlobalException {

    public DeliveryApplicationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
