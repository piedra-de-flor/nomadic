package com.example.Triple_clone.service.membership;

import com.example.Triple_clone.domain.entity.User;
import com.example.Triple_clone.dto.membership.*;
import com.example.Triple_clone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    @Transactional
    public UserResponseDto join(UserJoinRequestDto userDto) {

        // UserDto의 username을 이용해 DB에 존재하는지 확인
        if (repository.findOneWithAuthoritiesByEmail(userDto.toEntity().getEmail()).orElse(null)
                != null) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }

        // 권한정보를 포함한 User 정보를 생성
        User user = User.builder()
                .email(userDto.toEntity().getEmail())
                .password(userDto.toEntity().getPassword())
                .name(userDto.toEntity().getName())
                .role(userDto.toEntity().getRole())
                .build();

        repository.save(user);
        // 최정 설정한 User 정보를 DB에 저장
        return UserResponseDto.fromUser(user);
    }

    @Transactional
    public UserResponseDto login(LoginDto loginDto) {
        User user = repository.findByEmail(loginDto.email())
                .orElseThrow(() -> new NoSuchElementException("no user entity"));

        if (loginDto.password().equals(user.getPassword())) {
            log.info("[login] 계정을 찾았습니다. " + user);

            return UserResponseDto.fromUser(user);
        }
        throw new IllegalArgumentException("password wrong");
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

    public User findById(long userId) {
        return repository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("no user Entity"));
    }
}
