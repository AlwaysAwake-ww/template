package com.example.template.global.aop;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;

@Aspect
@Component
public class ApiHistoryAop {

    @After("within(com.example.template.api..*)")
    public void saveLog(){

        System.out.println("#### /api/* method called");
    }

}
