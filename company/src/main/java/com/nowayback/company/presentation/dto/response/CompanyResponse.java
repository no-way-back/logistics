package com.nowayback.company.presentation.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.nowayback.company.application.dto.result.CompanyResult;

public record CompanyResponse(
	UUID companyId,
	String companyName,
	UUID hubId,
	String address,
	UUID managerUserId,
	LocalDateTime createdAt,
	UUID createdBy,
	LocalDateTime updatedAt,
	UUID updatedBy
) {
	public static CompanyResponse from(CompanyResult result) {
		return new CompanyResponse(
			result.companyId(),
			result.companyName(),
			result.hubId(),
			result.address(),
			result.managerUserId(),
			result.createdAt(),
			result.createdBy(),
			result.updatedAt(),
			result.updatedBy()
		);
	}
}
