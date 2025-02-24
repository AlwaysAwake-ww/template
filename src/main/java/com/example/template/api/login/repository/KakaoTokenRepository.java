package com.example.template.api.login.repository;

import com.example.template.api.login.entity.KakaoToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KakaoTokenRepository extends JpaRepository<KakaoToken, Long> {

    Optional<KakaoToken> findByKakaoId(Long KakaoId);


}
