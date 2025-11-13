package com.nowayback.company.application;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nowayback.common.dto.PageResponse;
import com.nowayback.common.security.annotation.UserRole;
import com.nowayback.company.application.dto.command.UpdateCompanyCommand;
import com.nowayback.company.application.dto.result.DeleteCompanyResult;
import com.nowayback.company.application.exception.CompanyApplicationErrorCode;
import com.nowayback.company.application.exception.CompanyApplicationException;
import com.nowayback.company.domain.entity.Company;
import com.nowayback.company.infrastructure.client.HubClient;
import com.nowayback.company.infrastructure.client.UserClient;
import com.nowayback.company.application.dto.command.CreateCompanyCommand;
import com.nowayback.company.application.dto.result.CompanyResult;
import com.nowayback.company.domain.repository.CompanyRepository;
import com.nowayback.company.infrastructure.client.dto.HubResponse;
import com.nowayback.company.infrastructure.client.dto.UserResponse;

@Service
public class CompanyService {

	private final CompanyRepository companyRepository;
	private final HubClient hubClient;
	private final UserClient userClient;

	public CompanyService(CompanyRepository companyRepository, HubClient hubClient, UserClient userClient) {
		this.companyRepository = companyRepository;
		this.hubClient = hubClient;
		this.userClient = userClient;
	}

	@Transactional
	public CompanyResult createCompany(CreateCompanyCommand command, UUID createBy, UserRole role) {

		validateDuplicateCompanyName(command.companyName());

		validateHubExists(command.hubId());

		if (role == UserRole.HUB_MANAGER) {
			validateHubManagerPermission(createBy, command.hubId());
		}

		if (command.managerUserId() != null) {
			validateManager(command.managerUserId());
		}

		Company company = Company.createCompany(
			command.companyName(),
			command.hubId(),
			command.address(),
			command.managerUserId()
		);

		companyRepository.save(company);

		return CompanyResult.from(company);
	}

	public CompanyResult getCompany(UUID companyId) {

		Company company = companyRepository.findByCompanyIdAndDeletedAtIsNull(companyId)
			.orElseThrow(() -> new CompanyApplicationException(
				CompanyApplicationErrorCode.COMPANY_NOT_FOUND
			));

		return CompanyResult.from(company);
	}

	@Transactional(readOnly = true)
	public PageResponse<CompanyResult> getCompanies(
		UUID userId,
		UserRole role,
		String companyName,
		Pageable pageable
	) {
		Page<Company> companyPage;

		if (role == UserRole.HUB_MANAGER) {
			UUID hubId = getHubIdByManager(userId);
			companyPage = getCompaniesByHubAndName(hubId, companyName, pageable);
		} else {
			companyPage = getCompaniesByName(companyName, pageable);
		}

		Page<CompanyResult> resultPage = companyPage.map(CompanyResult::from);

		String sortBy = pageable.getSort().stream()
			.findFirst()
			.map(Sort.Order::getProperty)
			.orElse("createdAt");

		boolean isAsc = pageable.getSort().stream()
			.findFirst()
			.map(order -> order.getDirection() == Sort.Direction.ASC)
			.orElse(false);

		return PageResponse.fromPage(resultPage, sortBy, isAsc);
	}

	@Transactional
	public CompanyResult updateCompany(
		UUID companyId,
		UpdateCompanyCommand command,
		UUID updatedBy,
		UserRole role
	) {
		Company company = companyRepository.findByCompanyIdAndDeletedAtIsNull(companyId)
			.orElseThrow(() -> new CompanyApplicationException(
				CompanyApplicationErrorCode.COMPANY_NOT_FOUND
			));

		validateUpdatePermission(company, updatedBy, role);

		if (command.companyName() != null && !command.companyName().equals(company.getCompanyName())) {
			validateDuplicateCompanyNameForUpdate(command.companyName(), companyId);
		}

		if (command.hubId() != null && !command.hubId().equals(company.getHubId())) {
			validateHubExists(command.hubId());

			if (role == UserRole.HUB_MANAGER) {
				validateHubManagerPermission(updatedBy, command.hubId());
			}
		}

		if (command.managerUserId() != null) {
			validateManager(command.managerUserId());
		}

		company.updateCompany(
			command.companyName(),
			command.hubId(),
			command.address(),
			command.managerUserId()
		);

		return CompanyResult.from(company);
	}

	@Transactional
	public DeleteCompanyResult deleteCompany(UUID companyId, UUID deletedBy, UserRole role) {

		Company company = companyRepository.findByCompanyIdAndDeletedAtIsNull(companyId)
			.orElseThrow(() -> new CompanyApplicationException(
				CompanyApplicationErrorCode.COMPANY_NOT_FOUND
			));

		validateDeletePermission(company, deletedBy, role);

		company.deleteCompany(deletedBy);

		return new DeleteCompanyResult(companyId);
	}

	private void validateDuplicateCompanyName(String companyName) {
		if (companyRepository.existsByCompanyNameAndDeletedAtIsNull(companyName)) {
			throw new CompanyApplicationException(
				CompanyApplicationErrorCode.DUPLICATE_COMPANY_NAME
			);
		}
	}

	private void validateDuplicateCompanyNameForUpdate(String companyName, UUID companyId) {
		if (companyRepository.existsByCompanyNameAndCompanyIdNotAndDeletedAtIsNull(companyName, companyId)) {
			throw new CompanyApplicationException(
				CompanyApplicationErrorCode.DUPLICATE_COMPANY_NAME
			);
		}
	}

	private void validateHubExists(UUID hubId) {
		try {
			hubClient.getHub(hubId);
		} catch (Exception e) {
			throw new CompanyApplicationException(
				CompanyApplicationErrorCode.HUB_NOT_FOUND
			);
		}
	}

	private void validateHubManagerPermission(UUID managerId, UUID hubId) {
		try {
			HubResponse hub = hubClient.getHub(hubId);
			if (!hub.managerId().equals(managerId)) {
				throw new CompanyApplicationException(
					CompanyApplicationErrorCode.FORBIDDEN_NOT_HUB_MANAGER
				);
			}
		} catch (CompanyApplicationException e) {
			throw e;
		} catch (Exception e) {
			throw new CompanyApplicationException(
				CompanyApplicationErrorCode.FORBIDDEN_NOT_HUB_MANAGER
			);
		}
	}

	private void validateManager(UUID managerUserId) {
		try {
			UserResponse user = userClient.getUser(managerUserId);
			if (user.role() != UserRole.COMPANY_MANAGER) {
				throw new CompanyApplicationException(
					CompanyApplicationErrorCode.INVALID_MANAGER_ROLE
				);
			}
		} catch (CompanyApplicationException e) {
			throw e;
		} catch (Exception e) {
			throw new CompanyApplicationException(
				CompanyApplicationErrorCode.MANAGER_USER_NOT_FOUND
			);
		}
	}

	private void validateUpdatePermission(Company company, UUID userId, UserRole role) {
		if (role == UserRole.MASTER)
			return;

		if (role == UserRole.HUB_MANAGER) {
			UUID hubId = getHubIdByManager(userId);
			if (company.belongsToHub(hubId)) {
				throw new CompanyApplicationException(
					CompanyApplicationErrorCode.FORBIDDEN_NOT_HUB_MANAGER
				);
			}
			return;
		}

		if (role == UserRole.COMPANY_MANAGER) {
			if (!company.isManagedBy(userId)) {
				throw new CompanyApplicationException(
					CompanyApplicationErrorCode.FORBIDDEN_NOT_COMPANY_MANAGER
				);
			}
			return;
		}

		throw new CompanyApplicationException(
			CompanyApplicationErrorCode.FORBIDDEN_INSUFFICIENT_PERMISSION
		);
	}

	private void validateDeletePermission(Company company, UUID userId, UserRole role) {
		if (role == UserRole.MASTER)
			return;

		if (role == UserRole.HUB_MANAGER) {
			UUID hubId = getHubIdByManager(userId);
			if (company.belongsToHub(hubId)) {
				throw new CompanyApplicationException(
					CompanyApplicationErrorCode.FORBIDDEN_NOT_HUB_MANAGER
				);
			}
			return;
		}

		throw new CompanyApplicationException(
			CompanyApplicationErrorCode.FORBIDDEN_INSUFFICIENT_PERMISSION
		);
	}

	private UUID getHubIdByManager(UUID managerId) {
		try {
			return hubClient.getHubByManager(managerId).hubId();
		} catch (Exception e) {
			throw new CompanyApplicationException(
				CompanyApplicationErrorCode.FORBIDDEN_NOT_HUB_MANAGER
			);
		}
	}

	private Page<Company> getCompaniesByHubAndName(UUID hubId, String companyName, Pageable pageable) {
		if (companyName != null && !companyName.isBlank()) {
			return companyRepository.findByHubIdAndCompanyNameContainingAndDeletedAtIsNull(
				hubId, companyName, pageable
			);
		}
		return companyRepository.findByHubIdAndDeletedAtIsNull(hubId, pageable);
	}

	private Page<Company> getCompaniesByName(String companyName, Pageable pageable) {
		if (companyName != null && !companyName.isBlank()) {
			return companyRepository.findByCompanyNameContainingAndDeletedAtIsNull(companyName, pageable);
		}
		return companyRepository.findAllByDeletedAtIsNull(pageable);
	}
}
