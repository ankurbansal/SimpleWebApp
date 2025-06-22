package com.simplewebapp.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogEntryExitAspect {
    @Before("@annotation(com.simplewebapp.aop.LogEntryExit)")
    public void logEntry(JoinPoint joinPoint) {
        System.out.println("[ENTRY] " + joinPoint.getSignature());
    }

    @After("@annotation(com.simplewebapp.aop.LogEntryExit)")
    public void logExit(JoinPoint joinPoint) {
        System.out.println("[EXIT] " + joinPoint.getSignature());
    }
} 