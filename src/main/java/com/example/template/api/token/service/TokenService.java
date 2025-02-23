package com.example.template.api.token.service;


import com.example.template.api.login.config.KakaoProperties;
import com.example.template.util.jwt.config.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final KakaoProperties kakaoProperties;
    private final JwtProperties jwtProperties;

    public void saveKakaoAccessToken(String userId, String accessToken){

        String key = "kakao:access:"+userId;
        redisTemplate.opsForValue().set(key, accessToken, Duration.ofSeconds(21600));

    }


    public String getKakaoAccessToken(String userId){

        String key = "kakao:access:"+userId;

        return (String)redisTemplate.opsForValue().get(key);
    }

    public void saveJwtToken(String userId, String jwt){

        String key = "jwt:token:"+userId;

        redisTemplate.opsForValue().set(key, jwt, Duration.ofSeconds(Long.parseLong(jwtProperties.getExpirationTime())));
    }

    public String getJwtToken(String userId){

        String key = "jwt:token:"+userId;
        return (String) redisTemplate.opsForValue().get(key);
    }


}
