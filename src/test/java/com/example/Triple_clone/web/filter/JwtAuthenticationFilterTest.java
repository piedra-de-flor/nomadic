package com.example.Triple_clone.web.filter;

import com.example.Triple_clone.common.auth.JwtAuthFilter;
import com.example.Triple_clone.common.auth.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private JwtAuthFilter jwtAuthFilter;

    @Test
    void Jwt토큰_필터_성공_테스트() throws ServletException, IOException {
        String fakeToken = "fakeToken";

        when(jwtTokenProvider.validateToken(fakeToken)).thenReturn(true);

        Authentication fakeAuthentication = mock(Authentication.class);

        when(jwtTokenProvider.getAuthentication(fakeToken)).thenReturn(fakeAuthentication);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(request.getHeader("Authorization")).thenReturn("Bearer " + fakeToken);

        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        jwtAuthFilter.doFilter(request, response, chain);

        verify(jwtTokenProvider).validateToken(fakeToken);
        verify(jwtTokenProvider).getAuthentication(fakeToken);
        verify(securityContext).setAuthentication(fakeAuthentication);
        verify(chain).doFilter(request, response);
    }



    @Test
    void Jwt토큰_필터_실패_유효하지_않은_토큰_테스트() throws ServletException, IOException {
        String fakeToken = "invalidToken";

        when(jwtTokenProvider.validateToken(fakeToken)).thenReturn(false);

        Authentication fakeAuthentication = mock(Authentication.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(request.getHeader("Authorization")).thenReturn("Bearer " + fakeToken);

        jwtAuthFilter.doFilter(request, response, chain);

        verify(jwtTokenProvider).validateToken(fakeToken);
        verifyNoInteractions(fakeAuthentication);
        verify(chain).doFilter(request, response);
    }

    @Test
    void Jwt토큰_필터_실패_토큰_없음_테스트() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        Authentication fakeAuthentication = mock(Authentication.class);

        when(request.getHeader("Authorization")).thenReturn(null);
        jwtAuthFilter.doFilter(request, response, chain);

        verifyNoInteractions(jwtTokenProvider);
        verifyNoInteractions(fakeAuthentication);
        verify(chain).doFilter(request, response);
    }
}

