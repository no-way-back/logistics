package com.nowayback.delivery.domain.delivery.exception;

import com.nowayback.common.exception.ErrorCode;
import com.nowayback.common.exception.GlobalException;

public class DeliveryDomainException extends GlobalException {

    public DeliveryDomainException(ErrorCode errorCode) {
        super(errorCode);
    }
}
