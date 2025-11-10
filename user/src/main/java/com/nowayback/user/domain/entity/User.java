package com.nowayback.user.domain.entity;

import java.util.UUID;

import com.nowayback.common.audit.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.nowayback.common.security.annotation.UserRole;
import com.nowayback.user.application.exception.UserApplicationException;
import com.nowayback.user.domain.exception.UserDomainErrorCode;
import com.nowayback.user.domain.exception.UserDomainException;

@Entity
@Table(name = "p_users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "user_id")
	private UUID userId;

	@Column(name = "username", nullable = false, unique = true, length = 100)
	private String username;

	@Column(name = "password", nullable = false)
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false)
	private UserRole role;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private UserStatus status;

	@Column(name = "slack_id", length = 100)
	private String slackId;

	private User(String username, String password, UserRole role, String slackId) {
		this.username = username;
		this.password = password;
		this.role = role;
		this.slackId = slackId;
		this.status = UserStatus.PENDING;
	}

	public static User createUser(String username, String password, UserRole role, String slackId) {
		return new User(username, password, role, slackId);
	}

	public void approveSignup() {
		if (this.status != UserStatus.PENDING) {
			throw new UserDomainException(UserDomainErrorCode.ALREADY_PROCESSED);
		}
		this.status = UserStatus.APPROVED;
	}

	public void rejectSignup() {
		if (this.status != UserStatus.PENDING) {
			throw new UserDomainException(UserDomainErrorCode.ALREADY_PROCESSED);
		}
		this.status = UserStatus.REJECTED;
	}

	public void validateCanLogin() {
		if (this.status != UserStatus.APPROVED) {
			throw new UserDomainException(UserDomainErrorCode.INVALID_USER_STATUS);
		}
	}

	public void validateApprovalStatus(UserStatus status) {
		if (status != UserStatus.APPROVED && status != UserStatus.REJECTED) {
			throw new UserApplicationException(UserDomainErrorCode.INVALID_APPROVAL_STATUS);
		}
	}
}
