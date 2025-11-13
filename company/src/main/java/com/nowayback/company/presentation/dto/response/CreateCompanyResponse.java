package com.nowayback.company.presentation.dto.response;

import java.util.UUID;

import com.nowayback.company.application.dto.result.CompanyResult;

public record CreateCompanyResponse(
	UUID companyId,
	String companyName,
	String message
) {
	public static CreateCompanyResponse from(CompanyResult result) {
		return new CreateCompanyResponse(
			result.companyId(),
			result.companyName(),
			"업체가 생성되었습니다."
		);
	}
}
