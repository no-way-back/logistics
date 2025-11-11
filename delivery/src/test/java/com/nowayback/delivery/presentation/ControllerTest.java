package com.nowayback.delivery.presentation;

import com.nowayback.common.exception.GlobalExceptionHandler;
import com.nowayback.common.security.annotation.UserRole;
import com.nowayback.common.security.config.CommonWebConfig;
import com.nowayback.common.security.interceptor.JwtConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.UUID;

@Import({
        CommonWebConfig.class,
        GlobalExceptionHandler.class
})
public abstract class ControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    protected ResultActions performWithAuth(MockHttpServletRequestBuilder builder, UserRole role) throws Exception {
        UUID userId = UUID.randomUUID();
        String username = "test";

        return mockMvc.perform(
                builder
                        .header(JwtConstants.HEADER_USER_ID, userId.toString())
                        .header(JwtConstants.HEADER_USERNAME, username)
                        .header(JwtConstants.HEADER_ROLE, role.name())
        );
    }

    protected ResultActions performWithAuth(MockHttpServletRequestBuilder builder) throws Exception {
        return performWithAuth(builder, UserRole.MASTER);
    }
}
