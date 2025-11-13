package com.nowayback.company.presentation.dto.response;

import java.util.UUID;

import com.nowayback.company.application.dto.result.CompanyResult;

public record UpdateCompanyResponse(
	UUID companyId,
	String message
) {
	public static UpdateCompanyResponse from(CompanyResult result) {
		return new UpdateCompanyResponse(
			result.companyId(),
			"업체 정보가 수정되었습니다."
		);
	}
}
