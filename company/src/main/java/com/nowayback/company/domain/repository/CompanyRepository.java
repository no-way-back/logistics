package com.nowayback.company.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nowayback.company.domain.entity.Company;

public interface CompanyRepository {

	Company save(Company company);

	Optional<Company> findByCompanyIdAndDeletedAtIsNull(UUID companyId);

	boolean existsByCompanyNameAndDeletedAtIsNull(String companyName);

	boolean existsByCompanyNameAndCompanyIdNotAndDeletedAtIsNull(String companyName, UUID companyId);

	Page<Company> findAllByDeletedAtIsNull(Pageable pageable);

	Page<Company> findByHubIdAndDeletedAtIsNull(UUID hubId, Pageable pageable);

	Page<Company> findByCompanyNameContainingAndDeletedAtIsNull(String companyName, Pageable pageable);

	Page<Company> findByHubIdAndCompanyNameContainingAndDeletedAtIsNull(
		UUID hubId, String companyName, Pageable pageable
	);
}
