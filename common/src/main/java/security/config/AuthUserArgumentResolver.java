package security.config;

import java.util.UUID;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import jakarta.servlet.http.HttpServletRequest;
import security.annotation.AuthUser;
import security.annotation.CurrentUser;
import security.annotation.UserRole;
import security.interceptor.JwtConstants;

public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(CurrentUser.class)
			&& parameter.getParameterType().equals(AuthUser.class);
	}

	@Override
	public Object resolveArgument(
		MethodParameter parameter,
		ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest,
		WebDataBinderFactory binderFactory
	) {
		HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

		String userIdHeader = request.getHeader(JwtConstants.HEADER_USER_ID);
		String usernameHeader = request.getHeader(JwtConstants.HEADER_USERNAME);
		String roleHeader = request.getHeader(JwtConstants.HEADER_ROLE);

		if (userIdHeader == null || usernameHeader == null || roleHeader == null) {
			throw new IllegalArgumentException("인증 정보가 헤더에 존재하지 않습니다.");
		}

		try {
			UUID userId = UUID.fromString(userIdHeader);
			UserRole role = UserRole.valueOf(roleHeader);

			return new AuthUser(userId, usernameHeader, role);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("잘못된 인증 정보 형식입니다.", e);
		}
	}
}
