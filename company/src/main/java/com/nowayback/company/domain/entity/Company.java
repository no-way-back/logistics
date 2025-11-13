package com.nowayback.company.domain.entity;

import java.util.Optional;
import java.util.UUID;

import com.nowayback.common.audit.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "p_companies")
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Company extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "company_id", updatable = false, nullable = false)
	private UUID companyId;

	@Column(name = "company_name", nullable = false, length = 100)
	private String companyName;

	@Column(name = "hub_id", nullable = false)
	private UUID hubId;

	@Column(name = "address", nullable = false)
	private String address;

	@Column(name = "manager_user_id")
	private UUID managerUserId;

	private Company(String companyName, UUID hubId, String address, UUID managerUserId) {
		this.companyName = companyName;
		this.hubId = hubId;
		this.address = address;
		this.managerUserId = managerUserId;
	}

	public static Company createCompany(
		String companyName,
		UUID hubId,
		String address,
		UUID managerUserId
	) {
		validateCompanyName(companyName);
		validateHubId(hubId);
		validateAddress(address);

		return new Company(companyName, hubId, address, managerUserId);
	}

	public void updateCompany(
		String companyName,
		UUID hubId,
		String address,
		UUID managerUserId
	) {
		Optional.ofNullable(companyName).ifPresent(name -> {
			validateCompanyName(name);
			this.companyName = name;
		});

		Optional.ofNullable(hubId).ifPresent(id -> {
			validateHubId(id);
			this.hubId = id;
		});

		Optional.ofNullable(address).ifPresent(addr -> {
			validateAddress(addr);
			this.address = addr;
		});

		Optional.ofNullable(managerUserId).ifPresent(id -> {
			this.managerUserId = id;
		});
	}

	public void deleteCompany(UUID deletedBy) {
		this.softDelete(deletedBy);
	}

	public boolean belongsToHub(UUID hubId) {
		return !this.hubId.equals(hubId);
	}

	public boolean isManagedBy(UUID userId) {
		return this.managerUserId != null && this.managerUserId.equals(userId);
	}

	// 현재는 추가 비즈니스 규칙 없음 (ex. 금칙어 체크)
	private static void validateCompanyName(String companyName) {
	}

	private static void validateHubId(UUID hubId) {
	}

	private static void validateAddress(String address) {
	}
}
