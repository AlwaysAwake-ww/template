package com.example.template.api.login.service;

import com.example.template.api.login.config.KakaoProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


import java.util.Map;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final KakaoProperties kakaoProperties;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    // 로그인 uri 생성
    public String getKakaoLoginUri() {
        return "https://kauth.kakao.com/oauth/authorize?client_id="
                + kakaoProperties.getClientId()
                + "&redirect_uri="
                + kakaoProperties.getRedirectUri()
                + "&response_type=code";
    }

    // Access token 요청
    public String getAccessToken(String code){

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoProperties.getClientId());
        params.add("redirect_uri", kakaoProperties.getRedirectUri());
        params.add("code", code);

        // http 요청 header 설정
        HttpHeaders headers = new HttpHeaders();

        // header의 contentType : application/x-www-form-urlencoded -> form 데이터
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 카카오 api 요청
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        // 응답
        ResponseEntity<String> response = restTemplate.postForEntity(kakaoProperties.getTokenUri(), request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                Map<String, Object> body = objectMapper.readValue(response.getBody(), Map.class);
                return (String) body.get("access_token");
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse access token response.");
            }
        } else {
            throw new RuntimeException("Failed to get access token.");
        }
    }


    // 사용자 정보 요청
    public Map<String, Object> getUserInfo(String token){

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(kakaoProperties.getUserinfoUri(), HttpMethod.GET, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                return objectMapper.readValue(response.getBody(), Map.class);
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse user info response.");
            }
        } else {
            throw new RuntimeException("Failed to get user info.");
        }
    }

}
