package com.example.Triple_clone.configuration;

import io.github.bucket4j.Bucket;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class RateLimitAspectTest {

    private Bucket bucket;
    private RateLimitAspect aspect;
    private ProceedingJoinPoint pjp;

    @BeforeEach
    void setUp() {
        bucket = mock(Bucket.class);
        aspect = new RateLimitAspect(bucket);
        pjp = mock(ProceedingJoinPoint.class);
    }

    @Test
    @DisplayName("버킷에 토큰이 있을 때는 proceed 호출 후 결과 반환")
    void whenBucketHasToken_thenProceedAndReturnValue() throws Throwable {
        // given
        when(bucket.tryConsume(1)).thenReturn(true);
        String expected = "ok";
        when(pjp.proceed()).thenReturn(expected);

        // when
        Object actual = aspect.rateLimiting(pjp);

        // then
        verify(bucket, times(1)).tryConsume(1);
        verify(pjp, times(1)).proceed();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("버킷에 토큰이 없을 때는 IllegalArgumentException 발생")
    void whenBucketEmpty_thenThrowException() {
        // given
        when(bucket.tryConsume(1)).thenReturn(false);

        // when / then
        assertThatThrownBy(() -> aspect.rateLimiting(pjp))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("request pull exceed");

        verify(bucket, times(1)).tryConsume(1);
        verifyNoInteractions(pjp);
    }
}
