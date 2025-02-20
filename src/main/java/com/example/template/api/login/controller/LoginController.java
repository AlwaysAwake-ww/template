package com.example.template.api.login.controller;


import com.example.template.api.login.dto.KakaoLoginResponseDto;
import com.example.template.api.login.service.LoginService;
import com.example.template.util.jwt.provider.JwtTokenProvider;
import feign.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/oauth")
public class LoginController {

    private LoginService loginService;


    private final JwtTokenProvider jwtTokenProvider;

    // 카카오 로그인 URL 제공
    @GetMapping("/auth/kakao/login")
    public ResponseEntity<String> kakaoLogin() {
        String loginUrl = loginService.getKakaoLoginUri();
        return ResponseEntity.ok(loginUrl);
    }

    // 카카오 로그인 콜백
    @GetMapping("/auth/kakao/callback")
    public ResponseEntity<KakaoLoginResponseDto> kakaoCallback(@RequestParam String code) {
        String accessToken = loginService.getAccessToken(code);
        var userInfo = loginService.getUserInfo(accessToken);

        // 사용자 정보를 바탕으로 JWT 생성
        String jwtToken = jwtTokenProvider.createToken(userInfo);

        // DTO에 담아 응답
        KakaoLoginResponseDto responseDto = KakaoLoginResponseDto.builder()
                .status("success")
                .jwtToken(jwtToken)
                .userInfo(userInfo)
                .build();

        return ResponseEntity.ok(responseDto);
    }

    // 카카오 사용자 정보 가져오기
    @GetMapping("/auth/kakao/userinfo")
    public ResponseEntity<?> getUserInfo(@RequestParam String token) {
        var userInfo = loginService.getUserInfo(token);
        return ResponseEntity.ok(userInfo);
    }

}
