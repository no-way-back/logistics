package com.nowayback.product.application.product.exception;

import com.nowayback.common.exception.ErrorCode;
import com.nowayback.common.exception.GlobalException;

public class ProductApplicationException extends GlobalException {

    public ProductApplicationException(ProductApplicationErrorCode errorCode) {
        super(errorCode);
    }
}
