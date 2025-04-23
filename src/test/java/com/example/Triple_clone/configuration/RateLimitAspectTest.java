package com.example.Triple_clone.configuration;

import io.github.bucket4j.Bucket;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RateLimitAspectTest {

    @Mock
    private Bucket bucket;

    @Mock
    private ProceedingJoinPoint joinPoint;

    @InjectMocks
    private RateLimitAspect rateLimitAspect;

    @Test
    void tryConsumeSuccess_thenProceed() throws Throwable {
        // given
        when(bucket.tryConsume(1)).thenReturn(true);
        when(joinPoint.proceed()).thenReturn("success");

        // when
        Object result = rateLimitAspect.rateLimiting(joinPoint);

        // then
        assertEquals("success", result);
        verify(joinPoint, times(1)).proceed();
    }

    @Test
    void tryConsumeFail_thenThrowException() throws Throwable {
        // given
        when(bucket.tryConsume(1)).thenReturn(false);

        // when + then
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            rateLimitAspect.rateLimiting(joinPoint);
        });

        assertEquals("request pull exceed", ex.getMessage());
        verify(joinPoint, never()).proceed();
    }
}
