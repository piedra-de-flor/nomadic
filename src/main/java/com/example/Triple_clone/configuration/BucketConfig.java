package com.example.Triple_clone.configuration;

import com.example.Triple_clone.common.ratelimit.Bucket4jRateLimiter;
import com.example.Triple_clone.common.ratelimit.InMemoryBucketRegistry;
import com.example.Triple_clone.common.ratelimit.RateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BucketConfig {
    @Bean
    public InMemoryBucketRegistry inMemoryBucketRegistry() {
        long capacity = 500L;
        long refillPerSec = 500L;
        return new InMemoryBucketRegistry(capacity, refillPerSec);
    }

    @Bean
    public RateLimiter rateLimiter(InMemoryBucketRegistry registry) {
        return new Bucket4jRateLimiter(registry);
    }
}
