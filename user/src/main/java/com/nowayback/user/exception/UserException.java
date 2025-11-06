package com.nowayback.user.exception;

import exception.ErrorCode;
import exception.GlobalException;

public class UserException extends GlobalException {

	public UserException(ErrorCode errorCode) {
		super(errorCode);
	}
}
