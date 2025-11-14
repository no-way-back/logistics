package com.nowayback.company.domain.exception;

import com.nowayback.common.exception.ErrorCode;
import com.nowayback.common.exception.GlobalException;

public class CompanyDomainException extends GlobalException {

	public CompanyDomainException(ErrorCode errorCode) {
		super(errorCode);
	}
}
