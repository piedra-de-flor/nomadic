/*
package com.example.Triple_clone.web.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTest {

    @Mock
    private HttpServletRequest requestMock;

    @Mock
    private HttpServletResponse responseMock;

    @Mock
    private FilterChain filterChainMock;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void 필터_인증_성공_테스트() throws IOException, ServletException {
        // Arrange
        MockitoAnnotations.openMocks(this); // 초기화

        when(requestMock.getHeader("Authorization")).thenReturn("ADMIN");

        // Act
        jwtAuthenticationFilter.doFilter(requestMock, responseMock, filterChainMock);

        // Assert
        verify(filterChainMock, times(1)).doFilter(requestMock, responseMock);
    }

    @Test
    void 필터_인증_실패_테스트() throws IOException, ServletException {
        // Arrange
        MockitoAnnotations.openMocks(this);

        when(requestMock.getHeader("Authorization")).thenReturn("INVALID_TOKEN");

        // Act
        jwtAuthenticationFilter.doFilter(requestMock, responseMock, filterChainMock);

        // Assert
        verify(filterChainMock, never()).doFilter(requestMock, responseMock);
    }
}

*/
