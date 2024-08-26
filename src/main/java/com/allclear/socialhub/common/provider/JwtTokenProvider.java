package com.allclear.socialhub.common.provider;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.allclear.socialhub.common.exception.CustomException;
import com.allclear.socialhub.common.exception.ErrorCode;
import com.allclear.socialhub.user.domain.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
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
		String sub = user.getId() + "," + user.getUsername() + "," + user.getEmail();

		return BEARER + Jwts.builder()
				.subject(sub)
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
	 * 3. JWT 토큰에서 Payload 의 username 추출
	 * 작성자 : 김효진
	 *
	 * @param token
	 * @return String   username
	 */
	public String extractAccountFromToken(String token) {

		Claims claims = this.extractAllClaims(token);
		String payload = claims.getSubject();
		String[] result = payload.split(",");
		String username = result[0].trim();
		return username;
	}

	/**
	 * 3. JWT 토큰에서 이메일을 추출합니다.
	 * 작성자 : 배서진
	 *
	 * @param token JWT 토큰
	 * @return 이메일 주소
	 */
	public String extractEmailFromToken(String token) {
		// 모든 클레임 추출
		Claims claims = extractAllClaims(token);

		// sub 클레임에서 "username,email" 형식의 값을 가져옴
		String subject = claims.getSubject();

		// "username,email" 형식에서 ','로 분리하여 이메일 추출
		String[] parts = subject.split(",");
		return parts[1];  // 이메일 부분 반환
	}

}
