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

	public User(String username, String password, UserRole role, String slackId) {
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
		this.status = UserStatus.APPROVED;
	}

	public void rejectSignup() {
		this.status = UserStatus.REJECTED;
	}

	public void updateStatus(UserStatus status) {
		this.status = status;
	}

	public void updateSlackId(String slackId) {
		this.slackId = slackId;
	}

	public void updatePassword(String password) {
		this.password = password;
	}
}
