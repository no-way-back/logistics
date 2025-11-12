package com.nowayback.user.application.exception;

import org.springframework.http.HttpStatus;

import com.nowayback.common.exception.ErrorCode;

public enum UserApplicationErrorCode implements ErrorCode {

	// 조회 실패
	USER_NOT_FOUND("USER2001", "사용자를 찾을 수 없습니다", HttpStatus.NOT_FOUND),

	// 중복
	USER_ALREADY_EXISTS("USER2002", "이미 존재하는 사용자입니다", HttpStatus.CONFLICT),

	// 인증 실패
	INVALID_PASSWORD("USER2003", "비밀번호가 일치하지 않습니다", HttpStatus.UNAUTHORIZED),
	USER_NOT_APPROVED("USER2004", "승인되지 않은 사용자입니다", HttpStatus.FORBIDDEN),

	// 기타
	USER_ALREADY_DELETED("USER2005", "이미 삭제된 사용자입니다", HttpStatus.BAD_REQUEST),
	INVALID_APPROVAL_STATUS("USER2006", "유효하지 않은 승인 상태입니다", HttpStatus.BAD_REQUEST),
	INSUFFICIENT_PERMISSION("USER2007", "권한이 부족합니다", HttpStatus.FORBIDDEN);

	private final String code;
	private final String message;
	private final HttpStatus status;

	UserApplicationErrorCode(String code, String message, HttpStatus status) {
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
