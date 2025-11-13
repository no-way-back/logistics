package com.nowayback.product.domain.product.exception;

import com.nowayback.common.exception.GlobalException;

public class ProductDomainException extends GlobalException {

    public ProductDomainException(ProductDomainErrorCode errorCode) {
        super(errorCode);
    }
}
