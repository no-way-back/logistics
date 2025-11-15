package com.nowayback.order.application.exception;

import com.nowayback.common.exception.ErrorCode;
import com.nowayback.common.exception.GlobalException;

public class OrderApplicationException extends GlobalException {

    public OrderApplicationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
