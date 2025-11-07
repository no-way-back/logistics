package com.nowayback.user.domain.exception;

import org.springframework.http.HttpStatus;

import com.nowayback.common.exception.ErrorCode;

public enum UserDomainErrorCode implements ErrorCode {

	// 상태 관련
	ALREADY_PROCESSED("USER_1001","이미 처리된 회원가입 요청입니다", HttpStatus.BAD_REQUEST),
	INVALID_USER_STATUS("USER_1002", "유효하지 않은 사용자 상태입니다", HttpStatus.BAD_REQUEST),

	// 검증 관련
	INVALID_USERNAME_FORMAT("USER_1003","사용자 이름은 4-10자의 영문 소문자와 숫자만 가능합니다", HttpStatus.BAD_REQUEST),
	USERNAME_REQUIRED("USER_1004","사용자 이름은 필수입니다", HttpStatus.BAD_REQUEST),

	// 비밀번호 관련
	SAME_PASSWORD("USER_1005","현재 비밀번호와 동일합니다", HttpStatus.BAD_REQUEST),
	PASSWORD_TOO_SHORT("USER_1006","비밀번호는 최소 8자 이상이어야 합니다", HttpStatus.BAD_REQUEST),
	PASSWORD_MISSING_UPPERCASE("USER_1007","비밀번호는 대문자를 포함해야 합니다", HttpStatus.BAD_REQUEST),
	PASSWORD_MISSING_LOWERCASE("USER_1008","비밀번호는 소문자를 포함해야 합니다", HttpStatus.BAD_REQUEST),
	PASSWORD_MISSING_DIGIT("USER_1009","비밀번호는 숫자를 포함해야 합니다", HttpStatus.BAD_REQUEST),
	PASSWORD_MISSING_SPECIAL("USER_1010","비밀번호는 특수문자를 포함해야 합니다", HttpStatus.BAD_REQUEST),

	// 역할 관련
	SAME_ROLE("USER_1011","현재 역할과 동일합니다", HttpStatus.BAD_REQUEST),

	// Slack ID 관련
	INVALID_SLACK_ID("USER_1012","유효하지 않은 Slack ID입니다", HttpStatus.BAD_REQUEST);

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
