package com.nowayback.hub.application.exception;

import com.nowayback.common.exception.ErrorCode;
import com.nowayback.common.exception.GlobalException;

public class HubApplicationException extends GlobalException {
    public HubApplicationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
