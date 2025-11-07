package com.nowayback.user.application.dto.command;

import java.util.UUID;

import com.nowayback.user.domain.entity.UserStatus;

public record ApprovalCommand(
	UUID userId,
	UserStatus status,
	String reason,
	UUID approvedBy
) {
}