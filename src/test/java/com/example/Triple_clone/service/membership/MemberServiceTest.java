package com.example.Triple_clone.service.membership;

import com.example.Triple_clone.domain.entity.Member;
import com.example.Triple_clone.domain.vo.Role;
import com.example.Triple_clone.dto.membership.LoginDto;
import com.example.Triple_clone.dto.membership.UserJoinRequestDto;
import com.example.Triple_clone.dto.membership.UserResponseDto;
import com.example.Triple_clone.dto.membership.UserUpdateDto;
import com.example.Triple_clone.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {
    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private UserService userService;
    @Mock
    private Member member;
    @Mock
    private UserJoinRequestDto userJoinRequestDto;
    @Mock
    private LoginDto loginDto;
    @Mock
    private UserUpdateDto userUpdateDto;

    @Test
    void 회원가입_성공_테스트() {
        when(userJoinRequestDto.toEntity()).thenReturn(member);
        when(memberRepository.findByEmail(member.getEmail())).thenReturn(Optional.empty());
        when(member.getRole()).thenReturn(Role.valueOf("USER"));
        when(memberRepository.save(any(Member.class))).thenReturn(member);

        UserResponseDto resultDto = userService.join(userJoinRequestDto);

        assertNotNull(resultDto);
        verify(memberRepository, times(1)).findByEmail(userJoinRequestDto.email());
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    void 회원가입_실패_테스트() {
        when(memberRepository.findByEmail(member.getEmail())).thenReturn(Optional.of(member));

        assertThrows(RuntimeException.class, () -> userService.login(loginDto));
    }

    @Test
    void 로그인_성공_테스트() {
        when(memberRepository.findByEmail(loginDto.email())).thenReturn(Optional.of(member));
        when(loginDto.password()).thenReturn("test");
        when(member.getPassword()).thenReturn("test");
        when(member.getRole()).thenReturn(Role.valueOf("USER"));

        UserResponseDto result = userService.login(loginDto);

        assertNotNull(result);
    }

    @Test
    void 로그인_실패_유저_없음_테스트() {
        when(memberRepository.findByEmail(loginDto.email())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> userService.login(loginDto));
    }

    @Test
    void 로그인_실패_비밀번호_오류_테스트() {
        when(memberRepository.findByEmail(loginDto.email())).thenReturn(Optional.of(member));
        when(loginDto.password()).thenReturn("test");
        when(member.getPassword()).thenReturn("test123");

        assertThrows(IllegalArgumentException.class, () -> userService.login(loginDto));
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
        long userId = 1L;
        when(memberRepository.findById(userId)).thenReturn(Optional.of(member));

        long result = userService.delete(userId);

        verify(memberRepository, times(1)).delete(member);
        assertEquals(userId, result);
    }

    @Test
    void 삭제_실패_테스트() {
        long invalidUserId = 999L;
        when(memberRepository.findById(invalidUserId)).thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class, () -> userService.delete(invalidUserId));
    }
}
