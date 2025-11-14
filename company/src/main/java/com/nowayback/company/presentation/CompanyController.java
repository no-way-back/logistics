package com.nowayback.company.presentation;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nowayback.common.dto.PageResponse;
import com.nowayback.common.security.annotation.CurrentUser;
import com.nowayback.common.security.annotation.RequireRole;
import com.nowayback.common.security.annotation.UserRole;
import com.nowayback.common.security.annotation.AuthUser;

import com.nowayback.company.application.CompanyService;
import com.nowayback.company.application.dto.command.CreateCompanyCommand;
import com.nowayback.company.application.dto.command.UpdateCompanyCommand;
import com.nowayback.company.application.dto.result.CompanyResult;
import com.nowayback.company.application.dto.result.DeleteCompanyResult;
import com.nowayback.company.presentation.dto.request.CreateCompanyRequest;
import com.nowayback.company.presentation.dto.request.UpdateCompanyRequest;
import com.nowayback.company.presentation.dto.response.CompanyResponse;
import com.nowayback.company.presentation.dto.response.CreateCompanyResponse;
import com.nowayback.company.presentation.dto.response.DeleteCompanyResponse;
import com.nowayback.company.presentation.dto.response.UpdateCompanyResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/companies")
public class CompanyController {

	private final CompanyService companyService;

	public CompanyController(CompanyService companyService) {
		this.companyService = companyService;
	}

	@PostMapping
	@RequireRole({UserRole.MASTER, UserRole.HUB_MANAGER})
	public ResponseEntity<CreateCompanyResponse> createCompany(
		@Valid @RequestBody CreateCompanyRequest request,
		@CurrentUser AuthUser authUser
	) {
		CreateCompanyCommand command = new CreateCompanyCommand(
			request.companyName(),
			request.hubId(),
			request.address(),
			request.managerUserId()
		);

		CompanyResult result = companyService.createCompany(command, authUser.userId(), authUser.role());

		return ResponseEntity.status(HttpStatus.CREATED)
			.body(CreateCompanyResponse.from(result));
	}

	@GetMapping
	@RequireRole({UserRole.MASTER, UserRole.HUB_MANAGER, UserRole.DELIVERY_MANAGER, UserRole.COMPANY_MANAGER})
	public ResponseEntity<PageResponse<CompanyResponse>> getCompanies(
		@RequestParam(required = false) String companyName,
		@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC)
		Pageable page,
		@CurrentUser AuthUser authUser
	) {
		PageResponse<CompanyResult> results = companyService.getCompanies(
			authUser.userId(),
			authUser.role(),
			companyName,
			page
		);

		PageResponse<CompanyResponse> response = new PageResponse<>(
			results.currentPage(),
			results.pageSize(),
			results.totalPages(),
			results.totalElements(),
			results.sortBy(),
			results.isAsc(),
			results.items().stream()
				.map(CompanyResponse::from)
				.toList()
		);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/{companyId}")
	@RequireRole({UserRole.MASTER, UserRole.HUB_MANAGER, UserRole.DELIVERY_MANAGER, UserRole.COMPANY_MANAGER})
	public ResponseEntity<CompanyResponse> getCompany(
		@PathVariable UUID companyId
	) {
		CompanyResult result = companyService.getCompany(companyId);

		return ResponseEntity.ok(CompanyResponse.from(result));
	}

	@PutMapping("/{companyId}")
	@RequireRole({UserRole.MASTER, UserRole.HUB_MANAGER, UserRole.COMPANY_MANAGER})
	public ResponseEntity<UpdateCompanyResponse> updateCompany(
		@PathVariable UUID companyId,
		@Valid @RequestBody UpdateCompanyRequest request,
		@CurrentUser AuthUser authUser
	) {
		UpdateCompanyCommand command = new UpdateCompanyCommand(
			request.companyName(),
			request.hubId(),
			request.address(),
			request.managerUserId()
		);

		CompanyResult result = companyService.updateCompany(companyId, command, authUser.userId(), authUser.role());

		return ResponseEntity.ok(UpdateCompanyResponse.from(result));
	}

	@DeleteMapping("/{companyId}")
	@RequireRole({UserRole.MASTER, UserRole.HUB_MANAGER})
	public ResponseEntity<DeleteCompanyResponse> deleteCompany(
		@PathVariable UUID companyId,
		@CurrentUser AuthUser authUser
	) {
		DeleteCompanyResult result = companyService.deleteCompany(companyId, authUser.userId(), authUser.role());

		return ResponseEntity.ok(DeleteCompanyResponse.from(result));
	}
}
