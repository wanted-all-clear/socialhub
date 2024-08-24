package com.allclear.socialhub.user.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
public class UserEmailVerificationRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public UserEmailVerificationRepository(RedisTemplate<String, String> redisTemplate) {

        this.redisTemplate = redisTemplate;
    }

    /**
     * 인증 코드를 Redis에 저장합니다.
     * 작성자: 배서진
     *
     * @param email      인증 코드와 연결할 이메일 주소
     * @param token      저장할 인증 코드
     * @param expireTime 인증 코드의 유효 기간 (밀리초 단위)
     */
    public void saveVerificationToken(String email, String token, long expireTime) {

        redisTemplate.opsForValue().set(email, token, expireTime, TimeUnit.MILLISECONDS);
    }

    /**
     * 이메일 주소로 인증 코드를 조회합니다.
     * 작성자: 배서진
     *
     * @param email 인증 코드가 저장된 이메일 주소
     * @return 저장된 인증 코드 (없을 경우 null 반환)
     */
    public String getVerificationToken(String email) {

        return redisTemplate.opsForValue().get(email);
    }

    /**
     * 인증 코드 삭제
     * 재인증 시 충돌 방지 및, Redis 메모리 관리를 위하여 수동으로 삭제하는 메서드를 추가항
     * 작성자: 배서진
     *
     * @param email 인증 코드를 삭제할 이메일 주소
     */
    public void deleteVerificationToken(String email) {

        redisTemplate.delete(email);
    }

}
