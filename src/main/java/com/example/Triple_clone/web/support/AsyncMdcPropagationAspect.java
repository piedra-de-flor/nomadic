package com.example.Triple_clone.web.support;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Aspect
@Component
public class AsyncMdcPropagationAspect {

    @Around("@annotation(org.springframework.scheduling.annotation.Async)")
    public Object propagateMdc(ProceedingJoinPoint joinPoint) throws Throwable {
        Map<String, String> contextMap = MDC.getCopyOfContextMap();

        return CompletableFuture.supplyAsync(() -> {
            if (contextMap != null) {
                MDC.setContextMap(contextMap);
            }

            try {
                return joinPoint.proceed();
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            } finally {
                MDC.clear();
            }
        }).get();
    }
}
