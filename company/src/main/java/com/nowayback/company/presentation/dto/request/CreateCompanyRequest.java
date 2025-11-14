package com.nowayback.company.presentation.dto.request;

import java.util.UUID;

import com.nowayback.company.application.dto.command.CreateCompanyCommand;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateCompanyRequest(

	@NotBlank(message = "업체명은 필수입니다.")
	@Size(max = 100, message = "업체명은 100자를 초과할 수 없습니다.")
	String companyName,

	@NotNull(message = "허브 ID는 필수입니다.")
	UUID hubId,

	@NotBlank(message = "주소는 필수입니다.")
	@Size(max = 255, message = "주소는 255자를 초과할 수 없습니다.")
	String address,

	UUID managerUserId
) {
	public CreateCompanyCommand toCommand() {
		return new CreateCompanyCommand(
			companyName,
			hubId,
			address,
			managerUserId
		);
	}
}
