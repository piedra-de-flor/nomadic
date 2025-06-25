package com.example.Triple_clone.web.support;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WithSystemMdc {
    String uri() default "";
}
