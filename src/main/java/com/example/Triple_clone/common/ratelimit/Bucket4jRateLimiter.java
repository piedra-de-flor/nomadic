package com.example.Triple_clone.common.ratelimit;

import io.github.bucket4j.Bucket;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Bucket4jRateLimiter implements RateLimiter {
    private final InMemoryBucketRegistry registry;

    @Override
    public boolean tryAcquire(String key, int permits) {
        Bucket b = registry.get(key);
        return b.tryConsume(permits);
    }
}