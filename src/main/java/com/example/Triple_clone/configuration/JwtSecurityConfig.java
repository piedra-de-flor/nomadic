package com.example.Triple_clone.configuration;

import com.example.Triple_clone.service.JwtTokenProvider;
import com.example.Triple_clone.web.filter.JwtAuthenticationFilter;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private JwtTokenProvider tokenProvider;

    // TokenProvider 생성자 주입
    public JwtSecurityConfig(JwtTokenProvider tokenProvider) {

        this.tokenProvider = tokenProvider;
    }

    @Override
    public void configure(HttpSecurity http) {

        JwtAuthenticationFilter customFilter = new JwtAuthenticationFilter(tokenProvider);

        // JwtFilter를 이용해 Spring Security 필터에 등록
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
