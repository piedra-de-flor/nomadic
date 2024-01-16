package com.example.Triple_clone.configuration;

import com.example.Triple_clone.domain.vo.JwtAccessDeniedHandler;
import com.example.Triple_clone.domain.vo.JwtAuthenticationEntryPoint;
import com.example.Triple_clone.service.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity// 1. 웹 보안을 활성화 해주는 어노테이션 추가
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    // 2. h2-console 과 favicon에 접근하는 건 security에 걸러지지않도록 설정
    @Bean
    public WebSecurityCustomizer configure() {

        return (web) -> web.ignoring()
                .requestMatchers(new AntPathRequestMatcher("/h2-console/**"))
                .requestMatchers(new AntPathRequestMatcher("/favicon.ico"));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .exceptionHandling(
                        (handling) ->  // exceptionHandling 시 앞서 정의한 클래스를 추가
                                handling.authenticationEntryPoint(jwtAuthenticationEntryPoint)
                                        .accessDeniedHandler(jwtAccessDeniedHandler)
                )
                // H2-console 을 위한 설정 추가,
                .headers((header) -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))

                // 세션을 사용하지 안ㅇ힉 ㅒㄸ문에 STATELESSfh 설정
                .sessionManagement((session)->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))


                .authorizeHttpRequests((registry) ->
                        registry.requestMatchers(
                                        new AntPathRequestMatcher("/"),
                                        new AntPathRequestMatcher("/*"),
                                        new AntPathRequestMatcher("/api/hello"),
                                        new AntPathRequestMatcher("/api/authenticate"),
                                        new AntPathRequestMatcher("/join")
                                )
                                .permitAll()
                                .anyRequest().authenticated()
                )

                .apply(new JwtSecurityConfig(tokenProvider));  // JwtSecurityConfig 설정 추가


        return httpSecurity.build();
    }
}

