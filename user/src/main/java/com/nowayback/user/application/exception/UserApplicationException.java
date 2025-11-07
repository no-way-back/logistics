package com.nowayback.user.application.exception;

import com.nowayback.common.exception.ErrorCode;
import com.nowayback.common.exception.GlobalException;

public class UserApplicationException extends GlobalException {

	public UserApplicationException(ErrorCode errorCode) {
		super(errorCode);
	}
}
