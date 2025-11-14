package com.nowayback.company.presentation.dto.request;

import java.util.UUID;

import com.nowayback.company.application.dto.command.UpdateCompanyCommand;

import jakarta.validation.constraints.Size;

public record UpdateCompanyRequest(

	@Size(max = 100, message = "업체명은 100자를 초과할 수 없습니다.")
	String companyName,

	UUID hubId,

	@Size(max = 255, message = "주소는 255자를 초과할 수 없습니다.")
	String address,

	UUID managerUserId
) {
	public UpdateCompanyCommand toCommand() {
		return new UpdateCompanyCommand(
			companyName,
			hubId,
			address,
			managerUserId
		);
	}
}
