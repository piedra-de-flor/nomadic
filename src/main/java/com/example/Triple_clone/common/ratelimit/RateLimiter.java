package com.example.Triple_clone.common.ratelimit;

public interface RateLimiter {
    boolean tryAcquire(String key, int permits);
}
