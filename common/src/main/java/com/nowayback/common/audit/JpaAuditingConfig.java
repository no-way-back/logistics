package com.nowayback.common.audit;

import java.util.Optional;
import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import com.nowayback.common.security.interceptor.JwtConstants;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditingConfig {

	@Bean
	public AuditorAware<UUID> auditorProvider() {
		return () -> {
			try {
				ServletRequestAttributes attributes =
					(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

				if (attributes == null) {
					return Optional.empty();
				}

				HttpServletRequest request = attributes.getRequest();
				String userIdHeader = request.getHeader(JwtConstants.HEADER_USER_ID);

				if (userIdHeader != null && !userIdHeader.isEmpty()) {
					return Optional.of(UUID.fromString(userIdHeader));
				}

				return Optional.empty();
			} catch (Exception e) {
				return Optional.empty();
			}
		};
	}
}