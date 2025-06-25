package com.example.Triple_clone.web.support;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Aspect
@Component
public class MdcAspect {
    @Around("@annotation(annotation)")
    public Object around(ProceedingJoinPoint joinPoint, WithSystemMdc annotation) throws Throwable {
        String traceId = UUID.randomUUID().toString();
        String email = "system";
        String uri = !annotation.uri().isEmpty()
                ? annotation.uri()
                : "task::" + joinPoint.getSignature().getName();

        return proceedWithMdc(traceId, email, uri, joinPoint);
    }

    @Around("@annotation(org.springframework.scheduling.annotation.Scheduled)")
    public Object scheduledMdc(ProceedingJoinPoint joinPoint) throws Throwable {
        String traceId = UUID.randomUUID().toString();
        String email = "system";
        String uri = "scheduled::" + joinPoint.getSignature().toShortString();

        return proceedWithMdc(traceId, email, uri, joinPoint);
    }

    private Object proceedWithMdc(String traceId, String email, String uri, ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            MDC.put("traceId", traceId);
            MDC.put("email", email);
            MDC.put("uri", uri);

            return joinPoint.proceed();
        } finally {
            MDC.clear();
        }
    }
}
