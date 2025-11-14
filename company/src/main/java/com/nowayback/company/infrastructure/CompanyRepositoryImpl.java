package com.nowayback.company.infrastructure;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.nowayback.company.domain.entity.Company;
import com.nowayback.company.domain.repository.CompanyRepository;

@Repository
public class CompanyRepositoryImpl implements CompanyRepository {

	private final CompanyJpaRepository companyJpaRepository;

	public CompanyRepositoryImpl(CompanyJpaRepository companyJpaRepository) {
		this.companyJpaRepository = companyJpaRepository;
	}

	@Override
	public Company save(Company company) {
		return companyJpaRepository.save(company);
	}

	@Override
	public Optional<Company> findByCompanyIdAndDeletedAtIsNull(UUID companyId) {
		return companyJpaRepository.findByCompanyIdAndDeletedAtIsNull(companyId);
	}

	@Override
	public boolean existsByCompanyNameAndDeletedAtIsNull(String companyName) {
		return companyJpaRepository.existsByCompanyNameAndDeletedAtIsNull(companyName);
	}

	@Override
	public boolean existsByCompanyNameAndCompanyIdNotAndDeletedAtIsNull(String companyName, UUID companyId) {
		return companyJpaRepository.existsByCompanyNameAndCompanyIdNotAndDeletedAtIsNull(companyName, companyId);
	}

	@Override
	public Page<Company> findAllByDeletedAtIsNull(Pageable pageable) {
		return companyJpaRepository.findAllByDeletedAtIsNull(pageable);
	}

	@Override
	public Page<Company> findByHubIdAndDeletedAtIsNull(UUID hubId, Pageable pageable) {
		return companyJpaRepository.findByHubIdAndDeletedAtIsNull(hubId, pageable);
	}

	@Override
	public Page<Company> findByCompanyNameContainingAndDeletedAtIsNull(String companyName, Pageable pageable) {
		return companyJpaRepository.findByCompanyNameContainingAndDeletedAtIsNull(companyName, pageable);
	}

	@Override
	public Page<Company> findByHubIdAndCompanyNameContainingAndDeletedAtIsNull(
		UUID hubId, String companyName, Pageable pageable
	) {
		return companyJpaRepository.findByHubIdAndCompanyNameContainingAndDeletedAtIsNull(hubId, companyName, pageable);
	}
}
