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

    @Around("@annotation(annotation)")
    public Object around(ProceedingJoinPoint joinPoint, WithSystemMdc annotation) throws Throwable {
        String traceId = UUID.randomUUID().toString();
        String email = "system";
        String uri = !annotation.uri().isEmpty() ?
                annotation.uri() :
                "task::" + joinPoint.getSignature().getName();

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
