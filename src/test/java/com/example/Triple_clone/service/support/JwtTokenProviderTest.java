package com.example.Triple_clone.service.support;

import com.example.Triple_clone.common.auth.JwtToken;
import com.example.Triple_clone.common.auth.JwtTokenProvider;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;
    private final String secretKey = "YmFzZTY0c2VjcmV0a2V5YmFzZTY0c2VjcmV0a2V5YmFzZTY0"; // base64 encoded 256bit key

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider(secretKey);
    }

    @Test
    void generateToken_and_validate_success() {
        var auth = new UsernamePasswordAuthenticationToken("testUser", "password",
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));

        JwtToken token = jwtTokenProvider.generateToken(auth);

        assertNotNull(token.getAccessToken());
        assertNotNull(token.getRefreshToken());
        assertTrue(jwtTokenProvider.validateToken(token.getAccessToken()));
    }

    @Test
    void getAuthentication_from_token_success() {
        var auth = new UsernamePasswordAuthenticationToken("testUser", "password",
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
        JwtToken token = jwtTokenProvider.generateToken(auth);

        var authentication = jwtTokenProvider.getAuthentication(token.getAccessToken());

        assertEquals("testUser", authentication.getName());
        assertThat(authentication.getAuthorities()).extracting("authority").contains("ROLE_USER");
    }

    @Test
    void validateToken_fail_with_invalid_signature() {
    }

    @Test
    void validateToken_fail_with_expired_token() {
        var expiredToken = Jwts.builder()
                .setSubject("expiredUser")
                .claim("auth", "ROLE_USER")
                .setExpiration(new Date(System.currentTimeMillis() - 1000)) // 이미 만료됨
                .signWith(Keys.hmacShaKeyFor(io.jsonwebtoken.io.Decoders.BASE64.decode(secretKey)))
                .compact();

        assertFalse(jwtTokenProvider.validateToken(expiredToken));
    }
}
