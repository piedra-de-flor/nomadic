package com.example.Triple_clone.service.membership;

import com.example.Triple_clone.common.auth.JwtToken;
import com.example.Triple_clone.common.auth.JwtTokenProvider;
import com.example.Triple_clone.domain.member.application.UserService;
import com.example.Triple_clone.domain.member.domain.Member;
import com.example.Triple_clone.domain.member.infra.MemberRepository;
import com.example.Triple_clone.domain.member.web.dto.UserJoinRequestDto;
import com.example.Triple_clone.domain.member.web.dto.UserResponseDto;
import com.example.Triple_clone.domain.member.web.dto.UserUpdateDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {
    String email = "test@example.com";
    String password = "password";
    String jwtToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
    @Mock
    private MemberRepository memberRepository;
    @InjectMocks
    private UserService userService;
    @Mock
    private Member member;
    @Mock
    private UserJoinRequestDto userJoinRequestDto;
    @Mock
    private UserUpdateDto userUpdateDto;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private final AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
    @Mock
    private final JwtTokenProvider jwtTokenProvider = mock(JwtTokenProvider.class);
    @Mock
    AuthenticationManagerBuilder authenticationManagerBuilder;
    @Test
    void 회원가입_성공_테스트() {
        List<String> roles = new ArrayList<>();
        roles.add("USER");
        when(userJoinRequestDto.toEntity(any(), any())).thenReturn(member);
        when(memberRepository.findByEmail(member.getEmail())).thenReturn(Optional.empty());
        when(member.getRoles()).thenReturn(roles);
        when(memberRepository.save(any(Member.class))).thenReturn(member);
        when(passwordEncoder.encode(any())).thenReturn("test");

        UserResponseDto resultDto = userService.signUp(userJoinRequestDto);

        assertNotNull(resultDto);
        verify(memberRepository, times(1)).findByEmail(userJoinRequestDto.email());
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    void 회원가입_실패_테스트() {
        when(memberRepository.findByEmail(member.getEmail())).thenReturn(Optional.of(member));

        assertThrows(RuntimeException.class, () -> userService.signUp(userJoinRequestDto));
    }

    @Test
    void 로그인_성공_테스트() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(Authentication.class));
        when(jwtTokenProvider.generateToken(any(Authentication.class))).thenReturn(new JwtToken("Bearer", jwtToken, jwtToken));
        when(authenticationManagerBuilder.getObject()).thenReturn(authenticationManager);

        JwtToken result = userService.signIn(email, password);

        assertNotNull(result);
        assertEquals(jwtToken, result.getAccessToken());
    }

    @Test
    void 로그인_실패_유저_없음_테스트() {
        when(authenticationManagerBuilder.getObject()).thenReturn(authenticationManager);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(UsernameNotFoundException.class);

        assertThrows(UsernameNotFoundException.class, () -> userService.signIn(email, password));
    }

    @Test
    void 로그인_실패_비밀번호_오류_테스트() {
        String password = "wrongPassword";

        when(authenticationManagerBuilder.getObject()).thenReturn(authenticationManager);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(BadCredentialsException.class);

        assertThrows(BadCredentialsException.class, () -> userService.signIn(email, password));
    }

    @Test
    void 읽기_실패_테스트() {
        long invalidUserId = 999L;
        when(memberRepository.findById(invalidUserId)).thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class, () -> userService.read(invalidUserId));
    }

    @Test
    void 수정_성공_테스트() {
        when(memberRepository.findById(userUpdateDto.userId())).thenReturn(Optional.of(member));
        when(userUpdateDto.name()).thenReturn("test");
        when(userUpdateDto.password()).thenReturn("test");

        userService.update(userUpdateDto);

        verify(memberRepository, times(1)).findById(userUpdateDto.userId());
        verify(member, times(1)).update("test", "test");
    }

    @Test
    void 수정_실패_테스트() {
        when(memberRepository.findById(userUpdateDto.userId())).thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class, () -> userService.update(userUpdateDto));
    }

    @Test
    void 삭제_성공_테스트() {
        when(memberRepository.findByEmail(email)).thenReturn(Optional.of(member));
        when(member.getId()).thenReturn(1L);

        long result = userService.delete(email);

        verify(memberRepository, times(1)).delete(member);
        assertEquals(1L, result);
    }

    @Test
    void 삭제_실패_테스트() {
        String invalidEmail = "test";
        when(memberRepository.findByEmail(invalidEmail)).thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class, () -> userService.delete(invalidEmail));
    }
}
