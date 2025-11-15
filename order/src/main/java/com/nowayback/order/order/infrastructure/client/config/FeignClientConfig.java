package com.nowayback.order.order.infrastructure.client.config;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
@EnableFeignClients("com.nowayback.order.order.infrastructure.client")
public class FeignClientConfig {

    private static final String USER_ID_HEADER = "X-User-Id";
    private static final String USER_ROLE_HEADER = "X-User-Role";
    private static final String USER_NAME_HEADER = "X-Username";
    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            ServletRequestAttributes attrs =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs != null) {
                HttpServletRequest request = attrs.getRequest();
                String userId = request.getHeader(USER_ID_HEADER);
                String username = request.getHeader(USER_NAME_HEADER);
                String role = request.getHeader(USER_ROLE_HEADER);
                if (userId != null) template.header(USER_ID_HEADER, userId);
                if (username != null) template.header(USER_NAME_HEADER, username);
                if (role != null) template.header(USER_ROLE_HEADER, role);
            }
        };
    }
}