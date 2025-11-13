package com.nowayback.company.presentation.dto.response;

import java.util.UUID;

import com.nowayback.company.application.dto.result.DeleteCompanyResult;

public record DeleteCompanyResponse(
	UUID companyId,
	String message
) {
	public static DeleteCompanyResponse from(DeleteCompanyResult result) {
		return new DeleteCompanyResponse(
			result.companyId(),
			"업체가 삭제되었습니다."
		);
	}
}
