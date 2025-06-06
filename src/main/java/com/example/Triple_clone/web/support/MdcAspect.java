package com.example.Triple_clone.web.support;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.jboss.logging.MDC;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Aspect
@Component
public class MdcAspect {

    @Around("@annotation(com.example.Triple_clone.web.support.WithSystemMdc)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        String traceId = UUID.randomUUID().toString();
        try {
            MDC.put("traceId", traceId);
            MDC.put("email", "system");
            MDC.put("uri", "scheduler::" + joinPoint.getSignature().getName());
            return joinPoint.proceed();
        } finally {
            MDC.clear();
        }
    }
}
