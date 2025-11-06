package com.nowayback.user.exception;

import org.springframework.http.HttpStatus;

import exception.ErrorCode;

public enum UserErrorCode implements ErrorCode {

	USER_NOT_FOUND("USER_001", "사용자를 찾을 수 없습니다", HttpStatus.NOT_FOUND),
	USER_ALREADY_EXISTS("USER_002", "이미 존재하는 사용자입니다", HttpStatus.CONFLICT),
	INVALID_PASSWORD("USER_003", "비밀번호가 일치하지 않습니다", HttpStatus.UNAUTHORIZED),
	USER_NOT_APPROVED("USER_004", "승인되지 않은 사용자입니다", HttpStatus.FORBIDDEN),
	USER_ALREADY_DELETED("USER_005", "이미 삭제된 사용자입니다", HttpStatus.BAD_REQUEST);

	private final String code;
	private final String message;
	private final HttpStatus status;

	UserErrorCode(String code, String message, HttpStatus status) {
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
