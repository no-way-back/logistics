package com.nowayback.company.application.dto.command;

import java.util.UUID;

public record UpdateCompanyCommand(
	String companyName,
	UUID hubId,
	String address,
	UUID managerUserId
){
}
