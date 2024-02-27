package com.example.Triple_clone.web.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@WebFilter(urlPatterns = "/admin/*")
public class JwtAuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String token = httpRequest.getHeader("Authorization");

        if (token != null && token.equals("ADMIN")) {
            log.info("인증완료");
            chain.doFilter(request, response);
        } else {
            log.info("인증실패");
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "인증되지 않은 요청입니다.");
        }
    }
}