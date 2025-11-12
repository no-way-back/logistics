package com.nowayback.product.domain.stock.exception;

import com.nowayback.common.exception.GlobalException;

public class StockDomainException extends GlobalException {

    public StockDomainException(StockDomainErrorCode errorCode) {
        super(errorCode);
    }
}
