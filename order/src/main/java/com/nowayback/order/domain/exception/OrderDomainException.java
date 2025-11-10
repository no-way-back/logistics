package com.nowayback.order.domain.exception;


import com.nowayback.common.exception.ErrorCode;
import com.nowayback.common.exception.GlobalException;

public class OrderDomainException extends GlobalException {

    public OrderDomainException(ErrorCode errorCode) {
        super(errorCode);
    }
}
