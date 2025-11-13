package com.nowayback.product.application.stock.exception;

import com.nowayback.common.exception.ErrorCode;
import com.nowayback.common.exception.GlobalException;

public class StockApplicationException extends GlobalException {

    public StockApplicationException(StockApplicationErrorCode errorCode) {
        super(errorCode);
    }
}
