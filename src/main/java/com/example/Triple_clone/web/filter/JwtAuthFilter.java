package com.example.Triple_clone.web.filter;

import com.example.Triple_clone.domain.vo.AuthErrorCode;
import com.example.Triple_clone.web.exception.RestApiException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@WebFilter(urlPatterns = "/admin/*")
public class JwtAuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String token = httpRequest.getHeader("Authorization");

        if (token != null && token.equals("ADMIN")) {
            log.info("role={} 관리자 인증 완료", token);
            chain.doFilter(request, response);
        } else {
            log.info("role={} 관리자 인증 실패", token);
            throw new RestApiException(AuthErrorCode.AUTH_ERROR_CODE);
        }
    }
}
