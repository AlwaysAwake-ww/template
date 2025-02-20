package com.example.template.util.jwt.provider;


import com.example.template.util.jwt.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;

    private Key signingKey;



    // secretKey 초기화
    @PostConstruct
    protected void init(){

        this.signingKey = Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes());
    }

    // jwt 생성
    public String createToken(Map<String, Object> userInfo){

        // 클레임 설정
        // userInfo에서 id 가져와서 subject로 설정
        Claims claims = Jwts.claims().setSubject(userInfo.get("id").toString());

        claims.put("nickname", userInfo.get("nickname"));
        claims.put("profileImage", userInfo.get("profile_image"));

        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtProperties.getExpirationTime());


        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // JWT 유효성 및 만료 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // JWT에서 사용자 정보 추출
    public Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // JWT에서 사용자 ID 추출
    public String getUserIdFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }


}
