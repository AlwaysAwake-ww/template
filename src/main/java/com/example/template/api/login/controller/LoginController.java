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

    private final LoginService loginService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/kakao/login")
    public ResponseEntity<String> kakaoLogin() {
        String loginUrl = loginService.getKakaoLoginUri();
        return ResponseEntity.ok(loginUrl);
    }

    @GetMapping("/kakao/callback")
    public ResponseEntity<KakaoLoginResponseDto> kakaoCallback(@RequestParam String code) {
        String accessToken = loginService.getAccessToken(code);
        var userInfo = loginService.getUserInfo(accessToken);

        String jwtToken = jwtTokenProvider.createToken(userInfo);

        KakaoLoginResponseDto responseDto = KakaoLoginResponseDto.builder()
                .status("success")
                .jwtToken(jwtToken)
                .userInfo(userInfo)
                .build();

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/kakao/userinfo")
    public ResponseEntity<?> getUserInfo(@RequestParam String token) {
        var userInfo = loginService.getUserInfo(token);
        return ResponseEntity.ok(userInfo);
    }

}
