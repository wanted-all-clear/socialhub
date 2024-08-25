package com.allclear.socialhub.user;

import com.allclear.socialhub.user.repository.UserEmailVerificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@DataRedisTest
public class UserEmailVerificationRepositoryTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private UserEmailVerificationRepository userEmailVerificationRepository;

    @BeforeEach
    void setUp() {

        userEmailVerificationRepository = new UserEmailVerificationRepository(redisTemplate);
    }

    @Test
    void saveAndGetVerificationToken() {
        // 준비: 이메일과 인증 토큰, 만료 시간을 설정합니다.
        String email = "test@example.com";
        String token = "12345678";
        long expireTime = 600000L; // 10분

        // 실행: 이메일과 토큰을 Redis에 저장합니다.
        userEmailVerificationRepository.saveVerificationToken(email, token, expireTime);

        // 검증: 저장된 토큰을 Redis에서 가져와서 원래 토큰과 일치하는지 확인합니다.
        String retrievedToken = userEmailVerificationRepository.getVerificationToken(email);
        assertEquals(token, retrievedToken, "저장된 토큰은 조회된 토큰과 일치해야 합니다");
    }

    @Test
    void deleteVerificationToken() {
        // 준비: 이메일과 인증 토큰, 만료 시간을 설정합니다.
        String email = "test@example.com";
        String token = "12345678";
        long expireTime = 600000L; // 10분

        // 실행: 토큰을 Redis에 저장한 후 삭제합니다.
        userEmailVerificationRepository.saveVerificationToken(email, token, expireTime);
        userEmailVerificationRepository.deleteVerificationToken(email);

        // 검증: 토큰이 삭제된 후 해당 이메일로 조회했을 때 null이 반환되는지 확인합니다.
        String retrievedToken = userEmailVerificationRepository.getVerificationToken(email);
        assertNull(retrievedToken, "토큰 삭제 후에는 조회 시 null이 반환되어야 합니다");
    }

}
