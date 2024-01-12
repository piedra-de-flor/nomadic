package com.example.Triple_clone.service.membership;

import com.example.Triple_clone.domain.entity.User;
import com.example.Triple_clone.dto.membership.JwtTokenDto;
import com.example.Triple_clone.dto.membership.UserJoinRequestDto;
import com.example.Triple_clone.dto.membership.UserResponseDto;
import com.example.Triple_clone.dto.membership.UserUpdateDto;
import com.example.Triple_clone.repository.UserRepository;
import com.example.Triple_clone.service.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    public UserResponseDto join(UserJoinRequestDto userJoinRequestDto) {
        User user = userJoinRequestDto.toEntity();
        repository.save(user);
        return UserResponseDto.fromUser(user);
    }

    public JwtTokenDto login(String email, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        return jwtTokenProvider.generateToken(authentication);
    }

    public UserResponseDto read(long userId) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("no user entity for update"));

        return UserResponseDto.fromUser(user);
    }

    public void update(UserUpdateDto userUpdateDto) {
        User user = repository.findById(userUpdateDto.userId())
                .orElseThrow(() -> new NoSuchElementException("no user entity for update"));

        user.update(userUpdateDto.name(), userUpdateDto.password());
    }

    public long delete(long userId) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("no user entity for update"));

        repository.delete(user);
        return userId;
    }
}
