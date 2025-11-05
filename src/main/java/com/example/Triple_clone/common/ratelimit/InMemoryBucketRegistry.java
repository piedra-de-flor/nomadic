package com.example.Triple_clone.common.ratelimit;

import io.github.bucket4j.*;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class InMemoryBucketRegistry {

    private final ConcurrentMap<String, Bucket> buckets = new ConcurrentHashMap<>();
    private final long capacity;
    private final long refillTokensPerSec;

    public InMemoryBucketRegistry(long capacity, long refillTokensPerSec) {
        this.capacity = capacity;
        this.refillTokensPerSec = refillTokensPerSec;
    }

    public Bucket get(String key) {
        return buckets.computeIfAbsent(key, k -> Bucket.builder()
                .addLimit(Bandwidth.classic(capacity, Refill.greedy(refillTokensPerSec, Duration.ofSeconds(1))))
                .build()
        );
    }
}
