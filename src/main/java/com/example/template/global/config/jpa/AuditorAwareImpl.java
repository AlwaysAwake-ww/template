package com.example.template.global.config.jpa;
import ch.qos.logback.core.util.StringUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.util.StringUtils;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String>{

    @Autowired
    private HttpServletRequest httpServletRequest;


    @Override
    public Optional<String> getCurrentAuditor() {

        String modifiedBy = httpServletRequest.getRequestURI();

        if(!StringUtils.hasText(modifiedBy)){
            modifiedBy = "unknown";
        }                           
        return Optional.empty();
    }
}
