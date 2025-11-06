package com.nowayback.user.presentation.dto.request;

import com.nowayback.user.domain.entity.UserRole;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record SignupUserRequest(
	@NotBlank(message = "사용자 이름은 필수입니다")
	@Pattern(regexp = "^[a-z0-9]{4,10}$",
		message = "사용자 이름은 4-10자 영문 소문자와 숫자만 가능합니다")
	String username,

	@NotBlank(message = "비밀번호는 필수입니다")
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,15}$",
		message = "비밀번호는 8-15자 영문 대소문자, 숫자, 특수문자를 포함해야 합니다")
	String password,

	@NotNull(message = "역할은 필수입니다")
	UserRole role,

	String slackId
) {
}
