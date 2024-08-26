package com.allclear.socialhub.common.jwt;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.crypto.SecretKey;

import com.allclear.socialhub.common.exception.CustomException;
import com.allclear.socialhub.common.exception.ErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.allclear.socialhub.user.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

	@Value("${JWT_SECRET_KEY}")
	private String SECRET_KEY;

	private final String BEARER = "Bearer ";

	/**
	 * 1. 문자열을 SecretKey 타입으로 변환
	 * 작성자 : 김은정
	 *
	 * @return SecretKey key
	 */
	private SecretKey getSigningKey() {

		byte[] keyBytes = Decoders.BASE64.decode(this.SECRET_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	/**
	 * 2. JWT 토큰 생성
	 * 작성자 : 김은정
	 *
	 * @param user
	 * @return String jwtToken
	 */
	public String createToken(User user) {

		Date expiryDate = Date.from(
				Instant.now().plus(1, ChronoUnit.HOURS)
		);

		return BEARER + Jwts.builder()
				.claim("username", user.getUsername())
				.claim("email", user.getEmail())
				.issuedAt(new Date())
				.expiration(expiryDate)
				.signWith(this.getSigningKey())
				.compact();
	}

	/**
	 * 3. JWT 토큰에서 Payload 추출
	 * 작성자 : 김은정
	 *
	 * @param token
	 * @return Claims payload
	 */
	public Claims extractAllClaims(String token) {
		try {
			if (token.startsWith(BEARER)) {
				token = token.substring(BEARER.length());
			}
			Claims claims = Jwts.parser()
					.verifyWith(this.getSigningKey())
					.build()
					.parseSignedClaims(token)
					.getPayload();

			if (claims == null || claims.isEmpty()) {
				throw new CustomException(ErrorCode.JWT_CLAIMS_EMPTY);
			}

			return claims;

		} catch (ExpiredJwtException e) {
			throw new CustomException(ErrorCode.EXPIRED_JWT_TOKEN);
		} catch (Exception e) {
			throw new CustomException(ErrorCode.INVALID_JWT_TOKEN);
		}
	}

	/**
	 * JWT 토큰에서 비공개 클레임 username 추출
	 * 작성자 : 김은정
	 */
	public String extractUsername(Claims claims) {
		if (claims == null) {
			throw new CustomException(ErrorCode.INVALID_JWT_TOKEN);
		}
		return String.valueOf(claims.get("username"));
	}

	/**
	 * JWT 토큰에서 비공개 클레임 email 추출
	 * 작성자 : 김은정
	 */
	public String extractEmail(Claims claims) {
		if (claims == null) {
			throw new CustomException(ErrorCode.INVALID_JWT_TOKEN);
		}

		return String.valueOf(claims.get("email"));
	}

}
