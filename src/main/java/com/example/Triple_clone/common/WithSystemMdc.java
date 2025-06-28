package com.example.Triple_clone.common;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WithSystemMdc {
    String uri() default "";
}
