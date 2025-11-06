package com.nowayback.user.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ApprovalRequest(
	@NotBlank(message = "상태는 필수입니다.")
	@Pattern(regexp = "^(APPROVED|REJECTED)$", message = "상태는 APPROVED 또는 REJECTED만 가능합니다.")
	String status,

	String reason
) {}