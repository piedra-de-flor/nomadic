package com.example.Triple_clone.configuration;

import com.example.Triple_clone.web.support.MemberEmailAspectResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class EmailAspectConfig implements WebMvcConfigurer {

    private final MemberEmailAspectResolver memberEmailAspectResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(memberEmailAspectResolver);
    }
}
