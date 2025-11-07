package com.nowayback.user.domain.exception;

import com.nowayback.common.exception.ErrorCode;
import com.nowayback.common.exception.GlobalException;

public class UserDomainException extends GlobalException {

	public UserDomainException(ErrorCode errorCode) {
		super(errorCode);
	}
}
