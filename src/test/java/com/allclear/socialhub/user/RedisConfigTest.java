package com.allclear.socialhub.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RedisConfigTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    public void testRedisConnection() {
        // RedisTemplate을 통해 Redis에 데이터를 저장하고 읽어오는 작업
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String key = "testKey";
        String value = "testValue";

        // 데이터 저장
        valueOperations.set(key, value);

        // 저장된 데이터 읽어오기
        String storedValue = valueOperations.get(key);

        // 데이터가 제대로 저장되고 읽어왔는지 확인
        assertThat(storedValue).isEqualTo(value);

        System.out.println("Redis connection and operation test passed.");
    }


//    @Autowired
//    private RedisTemplate<String, String> redisTemplate;
//
//    @Test
//    public void testRedisConnection() {
//        // ValueOperations를 사용하여 Redis에 데이터를 저장하고 읽어오기
//        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
//        String key = "testKey";
//        String value = "testValue";
//
//        // 데이터 저장
//        valueOperations.set(key, value);
//
//        // 저장된 데이터 읽어오기
//        String storedValue = valueOperations.get(key);
//
//        // 데이터가 제대로 저장되고 읽어왔는지 확인
//        assertThat(storedValue).isEqualTo(value);
//
//        System.out.println("Redis connection and operation test passed.");
//    }

}
