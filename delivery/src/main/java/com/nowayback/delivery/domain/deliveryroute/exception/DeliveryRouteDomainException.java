package com.nowayback.delivery.domain.deliveryroute.exception;

import com.nowayback.common.exception.GlobalException;

public class DeliveryRouteDomainException extends GlobalException {

    public DeliveryRouteDomainException(DeliveryRouteDomainErrorCode errorCode) {
        super(errorCode);
    }
}
