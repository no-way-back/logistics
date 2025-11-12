package com.nowayback.company.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nowayback.company.domain.entity.Company;

public interface CompanyJpaRepository extends JpaRepository<Company, Long> {
}
