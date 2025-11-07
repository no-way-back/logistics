package com.nowayback.user.presentation.dto.request;

import com.nowayback.user.domain.entity.UserStatus;

import jakarta.validation.constraints.NotNull;

public record ApprovalRequest(
	@NotNull(message = "상태는 필수입니다.")
	UserStatus status,

	String reason

) {}