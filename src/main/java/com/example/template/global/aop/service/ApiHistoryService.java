package com.example.template.global.aop.service;


import com.example.template.global.aop.entity.ApiHistory;
import com.example.template.global.aop.repository.ApiHistoryRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApiHistoryService {

    private final ApiHistoryRepository apiHistoryRepository;


    public void saveHistory(JoinPoint joinPoint, Object result, HttpServletRequest request) throws JsonProcessingException {

        String requestUrl = request.getRequestURI();
        String requestIp;
        String xForwardedForHeader = request.getHeader("X-Forwarded-For");

        if (xForwardedForHeader == null || xForwardedForHeader.isEmpty()) {
            requestIp = request.getRemoteAddr();
        } else {
            requestIp = xForwardedForHeader.split(",")[0].trim();
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValueAsString(result);

        ApiHistory apiHistory = ApiHistory.builder()
                .url(requestUrl)
                .ip(requestIp)
                .result(result.toString())
                .build();

        apiHistoryRepository.save(apiHistory);
    }
}
