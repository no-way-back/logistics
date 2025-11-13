package com.nowayback.company.application.exception;

import com.nowayback.common.exception.ErrorCode;
import com.nowayback.common.exception.GlobalException;

public class CompanyApplicationException extends GlobalException {

	public CompanyApplicationException(ErrorCode errorCode) {
		super(errorCode);
	}
}
