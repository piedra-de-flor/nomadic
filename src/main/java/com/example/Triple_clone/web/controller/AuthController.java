package com.example.Triple_clone.web.controller;

import com.example.Triple_clone.dto.membership.JwtTokenDto;
import com.example.Triple_clone.dto.membership.LoginDto;
import com.example.Triple_clone.service.JwtTokenProvider;
import com.example.Triple_clone.web.filter.JwtAuthenticationFilter;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public AuthController(JwtTokenProvider tokenProvider,
                          AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<JwtTokenDto> authorize(@Valid @RequestBody LoginDto loginDto) {

        // LoginDto를 이용해 username과 password를 받고 UsernamePasswordAuthenticationToken을 생성합니다.
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDto.email(), loginDto.password());

        // authenticationToken을 이용해서 authentication 객체를 생성하기 위해 authenticate 메서드가 실행될 때,
        // CustomUserDetailsService 에서 구현한 loadUserByUsername 메서드가 실행되고 최종적으로 Authentication 객체가 생성됩니다.
        Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(authenticationToken);

        //  생성된 Authentication 객체를 SecurityContext에 저장합니다.
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //  Authentication 객체를  createToken 메서드를 통해  JWT Token을 생성합니다.
        String jwt = tokenProvider.createToken(authentication);


        HttpHeaders httpHeaders = new HttpHeaders();

        // 생성된 Jwt 토큰을 Response Header에 넣어줍니다.
        httpHeaders.add(JwtAuthenticationFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        // TokenDto 를 이용해 ResponseBody 에도 넣어 리턴합니다.
        return new ResponseEntity<>(new JwtTokenDto(jwt), httpHeaders, HttpStatus.OK);
    }
}
