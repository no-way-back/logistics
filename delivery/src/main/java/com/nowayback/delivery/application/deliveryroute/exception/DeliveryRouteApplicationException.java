package com.nowayback.delivery.application.deliveryroute.exception;

import com.nowayback.common.exception.GlobalException;

public class DeliveryRouteApplicationException extends GlobalException {

    public DeliveryRouteApplicationException(DeliveryRouteApplicationErrorCode errorCode) {
        super(errorCode);
    }
}
