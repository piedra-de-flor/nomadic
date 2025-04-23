package com.example.Triple_clone.configuration;

import io.github.bucket4j.Bucket;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class RateLimitAspect {
    private final Bucket bucket;

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping) && execution(* com.example.Triple_clone.web.controller.recommend.user.RecommendController.like(..))")
    public void likeMethod() {}

    @Around("likeMethod()")
    public Object rateLimiting(ProceedingJoinPoint joinPoint) throws Throwable {
        if (bucket.tryConsume(1)) {
            return joinPoint.proceed();
        } else {
            throw new IllegalArgumentException("request pull exceed");
        }
    }
}
