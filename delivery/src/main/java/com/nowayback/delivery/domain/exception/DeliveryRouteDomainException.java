package com.nowayback.delivery.domain.exception;

import com.nowayback.common.exception.GlobalException;

public class DeliveryRouteDomainException extends GlobalException {

    public DeliveryRouteDomainException(DeliveryRouteDomainErrorCode errorCode) {
        super(errorCode);
    }
}
