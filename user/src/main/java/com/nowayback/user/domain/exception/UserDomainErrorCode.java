package com.nowayback.user.domain.exception;

import org.springframework.http.HttpStatus;

import com.nowayback.common.exception.ErrorCode;

public enum UserDomainErrorCode implements ErrorCode {

	// 상태 관련
	ALREADY_PROCESSED("USER1001","이미 처리된 회원가입 요청입니다", HttpStatus.BAD_REQUEST),
	INVALID_USER_STATUS("USER1002", "유효하지 않은 사용자 상태입니다", HttpStatus.BAD_REQUEST),

	// 검증 관련
	INVALID_USERNAME_FORMAT("USER1003","사용자 이름은 4-10자의 영문 소문자와 숫자만 가능합니다", HttpStatus.BAD_REQUEST),
	USERNAME_REQUIRED("USER1004","사용자 이름은 필수입니다", HttpStatus.BAD_REQUEST),
	INVALID_APPROVAL_STATUS("USER1005","올바른 상태값이 아닙니다.", HttpStatus.BAD_REQUEST),

	// 삭제 관련
	USER_ALREADY_DELETED("USER1006", "이미 삭제된 사용자입니다.", HttpStatus.BAD_REQUEST);

	private final String code;
	private final String message;
	private final HttpStatus status;

	UserDomainErrorCode(String code, String message, HttpStatus status) {
		this.code = code;
		this.message = message;
		this.status = status;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public HttpStatus getStatus() {
		return status;
	}
}
