package com.nowayback.delivery.domain.exception;

import com.nowayback.common.exception.GlobalException;

public class DeliveryManagerDomainException extends GlobalException {

    public DeliveryManagerDomainException(DeliveryManagerDomainErrorCode errorCode) {
        super(errorCode);
    }
}
