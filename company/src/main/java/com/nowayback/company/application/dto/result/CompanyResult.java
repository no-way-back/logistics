package com.nowayback.company.application.dto.result;

import java.time.LocalDateTime;
import java.util.UUID;

import com.nowayback.company.domain.entity.Company;

public record CompanyResult (
	UUID companyId,
	String companyName,
	UUID hubId,
	String address,
	UUID managerUserId,
	LocalDateTime createdAt,
	UUID createdBy,
	LocalDateTime updatedAt,
	UUID updatedBy
){
	public static CompanyResult from(Company company) {
		return new CompanyResult(
			company.getCompanyId(),
			company.getCompanyName(),
			company.getHubId(),
			company.getAddress(),
			company.getManagerUserId(),
			company.getCreatedAt(),
			company.getCreatedBy(),
			company.getUpdatedAt(),
			company.getUpdatedBy()
		);
	}
}
