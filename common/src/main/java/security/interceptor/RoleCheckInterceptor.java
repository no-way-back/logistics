package security.interceptor;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import java.util.Arrays;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import security.annotation.UserRole;
import security.annotation.RequireRole;

public class RoleCheckInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(
		HttpServletRequest request,
		HttpServletResponse response,
		Object handler
	) throws Exception {
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}

		HandlerMethod handlerMethod = (HandlerMethod) handler;
		RequireRole requireRole = handlerMethod.getMethodAnnotation(RequireRole.class);

		if (requireRole == null) {
			// 권한 체크가 필요 없는 경우
			return true;
		}

		String roleHeader = request.getHeader(JwtConstants.HEADER_ROLE);
		if (roleHeader == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write("{\"code\":\"UNAUTHORIZED\",\"message\":\"인증이 필요합니다.\"}");
			return false;
		}

		try {
			UserRole userRole = UserRole.valueOf(roleHeader);
			UserRole[] allowedRoles = requireRole.value();

			boolean hasPermission = Arrays.asList(allowedRoles).contains(userRole);

			if (!hasPermission) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				response.setContentType("application/json;charset=UTF-8");
				response.getWriter().write("{\"code\":\"FORBIDDEN\",\"message\":\"접근 권한이 없습니다.\"}");
				return false;
			}

			return true;
		} catch (IllegalArgumentException e) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write("{\"code\":\"INVALID_ROLE\",\"message\":\"잘못된 권한 정보입니다.\"}");
			return false;
		}
	}
}
