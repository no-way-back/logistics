package com.nowayback.user.exception;

import com.nowayback.common.exception.ErrorCode;
import com.nowayback.common.exception.GlobalException;

public class UserException extends GlobalException {

	public UserException(ErrorCode errorCode) {
		super(errorCode);
	}
}
