package com.nowayback.delivery.domain.deliverymanager.exception;

import com.nowayback.common.exception.GlobalException;

public class DeliveryManagerDomainException extends GlobalException {

    public DeliveryManagerDomainException(DeliveryManagerDomainErrorCode errorCode) {
        super(errorCode);
    }
}
