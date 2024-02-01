package com.example.Triple_clone.service.membership;

import com.example.Triple_clone.domain.entity.User;
import com.example.Triple_clone.domain.vo.Role;
import com.example.Triple_clone.dto.membership.LoginDto;
import com.example.Triple_clone.dto.membership.UserJoinRequestDto;
import com.example.Triple_clone.dto.membership.UserResponseDto;
import com.example.Triple_clone.dto.membership.UserUpdateDto;
import com.example.Triple_clone.repository.UserRepository;
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
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;
    @Mock
    private User user;
    @Mock
    private UserJoinRequestDto userJoinRequestDto;
    @Mock
    private LoginDto loginDto;
    @Mock
    private UserUpdateDto userUpdateDto;

    @Test
    void 회원가입_성공_테스트() {
        when(userJoinRequestDto.toEntity()).thenReturn(user);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(user.getRole()).thenReturn(Role.valueOf("USER"));

        UserResponseDto resultDto = userService.join(userJoinRequestDto);

        assertNotNull(resultDto);
        verify(userRepository, times(1)).findByEmail(userJoinRequestDto.email());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void 회원가입_실패_테스트() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        assertThrows(RuntimeException.class, () -> userService.login(loginDto));
    }

    @Test
    void 로그인_성공_테스트() {
        when(userRepository.findByEmail(loginDto.email())).thenReturn(Optional.of(user));
        when(loginDto.password()).thenReturn("test");
        when(user.getPassword()).thenReturn("test");
        when(user.getRole()).thenReturn(Role.valueOf("USER"));

        UserResponseDto result = userService.login(loginDto);

        assertNotNull(result);
    }

    @Test
    void 로그인_실패_유저_없음_테스트() {
        when(userRepository.findByEmail(loginDto.email())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> userService.login(loginDto));
    }

    @Test
    void 로그인_실패_비밀번호_오류_테스트() {
        when(userRepository.findByEmail(loginDto.email())).thenReturn(Optional.of(user));
        when(loginDto.password()).thenReturn("test");
        when(user.getPassword()).thenReturn("test123");

        assertThrows(IllegalArgumentException.class, () -> userService.login(loginDto));
    }

    @Test
    void 읽기_실패_테스트() {
        long invalidUserId = 999L;
        when(userRepository.findById(invalidUserId)).thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class, () -> userService.read(invalidUserId));
    }

    @Test
    void 수정_성공_테스트() {
        when(userRepository.findById(userUpdateDto.userId())).thenReturn(Optional.of(user));
        when(userUpdateDto.name()).thenReturn("test");
        when(userUpdateDto.password()).thenReturn("test");

        userService.update(userUpdateDto);

        verify(userRepository, times(1)).findById(userUpdateDto.userId());
        verify(user, times(1)).update("test", "test");
    }

    @Test
    void 수정_실패_테스트() {
        when(userRepository.findById(userUpdateDto.userId())).thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class, () -> userService.update(userUpdateDto));
    }

    @Test
    void 삭제_성공_테스트() {
        long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        long result = userService.delete(userId);

        verify(userRepository, times(1)).delete(user);
        assertEquals(userId, result);
    }

    @Test
    void 삭제_실패_테스트() {
        long invalidUserId = 999L;
        when(userRepository.findById(invalidUserId)).thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class, () -> userService.delete(invalidUserId));
    }
}
