package com.nowayback.gateway.filter;

import java.security.Key;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {

	private final Key key;

	public JwtAuthenticationFilter(@Value("${jwt.secret}") String secret) {
		super(Config.class);
		this.key = Keys.hmacShaKeyFor(secret.getBytes());
	}

	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();

			String path = request.getURI().getPath();
			if (isPublicPath(path)) {
				return chain.filter(exchange);
			}

			String authHeader = request.getHeaders().getFirst("Authorization");
			if (authHeader == null || !authHeader.startsWith("Bearer ")) {
				return onError(exchange, "인증 토큰이 없습니다.", HttpStatus.UNAUTHORIZED);
			}

			String token = authHeader.substring(7);

			try {
				Claims claims = Jwts.parserBuilder()
					.setSigningKey(key)
					.build()
					.parseClaimsJws(token)
					.getBody();

				String userId = claims.getSubject();
				String username = claims.get("username", String.class);
				String role = claims.get("role", String.class);

				ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
					.header(JwtConstants.HEADER_USER_ID, userId)
					.header(JwtConstants.HEADER_USERNAME, username)
					.header(JwtConstants.HEADER_ROLE, role)
					.build();

				ServerWebExchange mutatedExchange = exchange.mutate()
					.request(mutatedRequest)
					.build();

				return chain.filter(mutatedExchange);

			} catch (Exception e) {
				return onError(exchange, "유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED);
			}
		};
	}

	/**
	 * 인증이 필요 없는 공개 경로 확인
	 */
	private boolean isPublicPath(String path) {
		return path.equals("/users/signup") ||
			path.equals("/users/login");
	}

	/**
	 * 에러 응답 처리
	 */
	private Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus status) {
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(status);
		response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");

		String body = String.format("{\"code\":\"UNAUTHORIZED\",\"message\":\"%s\"}", message);

		return response.writeWith(
			Mono.just(response.bufferFactory().wrap(body.getBytes()))
		);
	}

	public static class Config {
		// 필요시 설정값 추가
	}
}
