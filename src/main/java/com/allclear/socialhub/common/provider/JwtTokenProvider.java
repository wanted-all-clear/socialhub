package com.allclear.socialhub.common.provider;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.allclear.socialhub.common.exception.ErrorCode;
import com.allclear.socialhub.user.domain.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

	@Value("${JWT_SECRET_KEY}")
	private String SECRET_KEY;

	/**
	 * 1. 문자열을 SecretKey 타입으로 변환
	 * 작성자 : 김은정
	 * @return SecretKey key
	 */
	private SecretKey getSigningKey() {
		byte[] keyBytes = Decoders.BASE64.decode(this.SECRET_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	/**
	 * 2. JWT 토큰 생성
	 * 작성자 : 김은정
	 * @param user
	 * @return String jwtToken
	 */
	public String createToken(User user) {
		Date expiryDate = Date.from(
				Instant.now().plus(1, ChronoUnit.HOURS)
		);
		String sub = user.getUsername() + "," + user.getEmail();

		return Jwts.builder()
				.subject(sub)
				.issuedAt(new Date())
				.expiration(expiryDate)
				.signWith(this.getSigningKey())
				.compact();
	}

	/**
	 * 3. JWT 토큰에서 Payload 추출
	 * 작성자 : 김은정
	 * @param token
	 * @return Claims payload
	 */
	public Claims extractAllClaims(String token) {
		try {
			return Jwts.parser()
					.verifyWith(this.getSigningKey())
					.build()
					.parseSignedClaims(token)
					.getPayload();

		} catch (JwtException ex) {
			throw new RuntimeException(ErrorCode.EXPIRED_JWT_TOKEN.getMessage());
		}
	}
}
