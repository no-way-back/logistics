package com.nowayback.company.application.exception;

import org.springframework.http.HttpStatus;

import com.nowayback.common.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CompanyApplicationErrorCode implements ErrorCode {

	// 업체 조회 관련
	COMPANY_NOT_FOUND("COMPANY2001", "업체를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

	// 업체 생성 관련
	DUPLICATE_COMPANY_NAME("COMPANY2002", "이미 존재하는 업체명입니다.", HttpStatus.CONFLICT),
	HUB_NOT_FOUND("COMPANY2003", "허브를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
	MANAGER_USER_NOT_FOUND("COMPANY2004", "담당자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
	INVALID_MANAGER_ROLE("COMPANY2005", "담당자는 COMPANY_MANAGER 권한이어야 합니다.", HttpStatus.BAD_REQUEST),

	// 업체 수정 관련
	CANNOT_CHANGE_TO_DELETED_HUB("COMPANY2006", "삭제된 허브로 변경할 수 없습니다.", HttpStatus.CONFLICT),
	CANNOT_ASSIGN_DELETED_MANAGER("COMPANY2007", "삭제된 사용자를 담당자로 지정할 수 없습니다.", HttpStatus.CONFLICT),

	// 권한 관련
	FORBIDDEN_NOT_HUB_MANAGER("COMPANY2008", "해당 허브의 관리자가 아닙니다.", HttpStatus.CONFLICT),
	FORBIDDEN_NOT_COMPANY_MANAGER("COMPANY2009", "해당 업체의 담당자가 아닙니다.", HttpStatus.CONFLICT),
	FORBIDDEN_INSUFFICIENT_PERMISSION("COMPANY2010", "권한이 부족합니다.", HttpStatus.CONFLICT);


	private final String code;
	private final String message;
	private final HttpStatus status;

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
