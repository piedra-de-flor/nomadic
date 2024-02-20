package com.example.Triple_clone.configuration;

import io.github.bucket4j.Bucket;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;

@Aspect
@Component
@RequiredArgsConstructor
public class RateLimitAspect {
    private final Bucket bucket;
    private final BlockingQueue<ProceedingJoinPoint> queue;

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping) && execution(* com.example.Triple_clone.web.controller.recommend.user.RecommendController.like(..))")
    public void likeMethod() {
    }

    @Around("likeMethod()")
    public Object rateLimiting(ProceedingJoinPoint joinPoint) throws Throwable {
        if (bucket.tryConsume(1)) {
            return joinPoint.proceed();
        } else {
            queue.put(joinPoint);
            return queue.size();
        }
    }
}
