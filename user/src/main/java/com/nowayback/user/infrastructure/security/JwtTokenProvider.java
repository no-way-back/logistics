package com.nowayback.user.infrastructure.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import com.nowayback.common.security.annotation.UserRole;

@Slf4j
@Component
public class JwtTokenProvider {

	private final SecretKey secretKey;
	private final long expirationTime;

	public JwtTokenProvider(
		@Value("${jwt.secret}") String secret,
		@Value("${jwt.expiration-time}") long expirationTime
	) {
		this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
		this.expirationTime = expirationTime;
	}

	public String createToken(UUID userId, String username, UserRole role) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + expirationTime);

		return Jwts.builder()
			.setSubject(userId.toString())
			.claim("username", username)
			.claim("role", role.name())
			.setIssuedAt(now)
			.setExpiration(expiryDate)
			.signWith(secretKey, SignatureAlgorithm.HS256)
			.compact();
	}
}
