package com.nowayback.hub.domain.hub.exception;

import com.nowayback.common.exception.ErrorCode;
import com.nowayback.common.exception.GlobalException;

public class HubDomainException extends GlobalException {
    public HubDomainException(ErrorCode errorCode) {
        super(errorCode);
    }
}
