package com.nowayback.user.domain.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserStatus {
	PENDING("승인 대기"),
	APPROVED("승인됨"),
	REJECTED("거부됨");

	private final String description;
}
