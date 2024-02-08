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
        repository.findByEmail(userDto.toEntity().getEmail())
                .orElseThrow(() -> new IllegalArgumentException("already register email"));

        User user = User.builder()
                .email(userDto.toEntity().getEmail())
                .password(userDto.toEntity().getPassword())
                .name(userDto.toEntity().getName())
                .role(userDto.toEntity().getRole())
                .build();

        User savedUser = repository.save(user);
        return UserResponseDto.fromUser(savedUser);
    }

    @Transactional
    public UserResponseDto login(LoginDto loginDto) {
        User user = repository.findByEmail(loginDto.email())
                .orElseThrow(() -> new NoSuchElementException("no user entity"));

        if (loginDto.password().equals(user.getPassword())) {
            return UserResponseDto.fromUser(user);
        }
        throw new IllegalArgumentException("password wrong");
    }

    public UserResponseDto read(long userId) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("no user entity"));

        return UserResponseDto.fromUser(user);
    }

    @Transactional
    public void update(UserUpdateDto userUpdateDto) {
        User user = repository.findById(userUpdateDto.userId())
                .orElseThrow(() -> new NoSuchElementException("no user entity"));

        user.update(userUpdateDto.name(), userUpdateDto.password());
    }

    public long delete(long userId) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("no user entity"));

        repository.delete(user);
        return userId;
    }

    public User findById(long userId) {
        return repository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("no user Entity"));
    }
}
