package com.nowayback.order.domain.exception;

import exception.ErrorCode;
import exception.GlobalException;

public class OrderDomainException extends GlobalException {

    public OrderDomainException(ErrorCode errorCode) {
        super(errorCode);
    }
}
