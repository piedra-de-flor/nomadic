package com.example.Triple_clone.configuration;

import com.example.Triple_clone.common.error.TooManyRequestsException;
import com.example.Triple_clone.common.ratelimit.RateLimited;
import com.example.Triple_clone.common.ratelimit.RateLimiter;
import io.github.bucket4j.Bucket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.*;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.*;
import org.springframework.expression.*;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class RateLimitAspect {
    private static final String FALLBACK_KEY = "global";

    private final RateLimiter limiter;
    private final HttpServletRequest httpRequest;

    private final ParameterNameDiscoverer paramNameDiscoverer = new DefaultParameterNameDiscoverer();
    private final ExpressionParser parser = new SpelExpressionParser();

    @Around("@annotation(rateLimited)")
    public Object around(ProceedingJoinPoint pjp, RateLimited rateLimited) throws Throwable {
        String key = resolveKey(rateLimited.key(), pjp);
        int permits = rateLimited.permits();

        if (limiter.tryAcquire(key, permits)) {
            return pjp.proceed();
        }
        throw new TooManyRequestsException(rateLimited.retryAfterSeconds());
    }

    private String resolveKey(String spelOrLiteral, ProceedingJoinPoint pjp) {
        if ("#ip".equals(spelOrLiteral)) {
            String ip = httpRequest.getHeader("X-Forwarded-For");
            if (ip == null || ip.isBlank()) ip = httpRequest.getRemoteAddr();
            return "ip:" + ip;
        }

        try {
            MethodSignature ms = (MethodSignature) pjp.getSignature();
            String[] paramNames = paramNameDiscoverer.getParameterNames(ms.getMethod());
            StandardEvaluationContext ctx = new StandardEvaluationContext();
            Object[] args = pjp.getArgs();

            if (paramNames != null) {
                for (int i = 0; i < paramNames.length; i++) {
                    ctx.setVariable(paramNames[i], args[i]);
                }
            }

            Expression expr = parser.parseExpression(spelOrLiteral);
            Object val = expr.getValue(ctx);
            if (val != null) return String.valueOf(val);
        } catch (org.springframework.expression.ParseException | org.springframework.expression.EvaluationException e) {
            MethodSignature ms = (MethodSignature) pjp.getSignature();
            log.warn("[ratelimit] key evaluation failed at {}.{}(..) expr='{}': {}",
                    ms.getDeclaringTypeName(), ms.getName(), spelOrLiteral, e.getMessage());
            return (spelOrLiteral != null && spelOrLiteral.startsWith("#")) ? FALLBACK_KEY : spelOrLiteral;
        } catch (Exception e) {
            MethodSignature ms = (MethodSignature) pjp.getSignature();
            log.warn("[ratelimit] unexpected error resolving key at {}.{}(..) expr='{}': {}",
                    ms.getDeclaringTypeName(), ms.getName(), spelOrLiteral, e.toString());
            return (spelOrLiteral != null && spelOrLiteral.startsWith("#")) ? FALLBACK_KEY : spelOrLiteral;
        }
        return spelOrLiteral;
    }
}
