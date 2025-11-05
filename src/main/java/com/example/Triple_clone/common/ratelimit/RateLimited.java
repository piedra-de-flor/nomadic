package com.example.Triple_clone.common.ratelimit;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimited {
    String key() default "global";
    int permits() default 1;
    int retryAfterSeconds() default 1;
}