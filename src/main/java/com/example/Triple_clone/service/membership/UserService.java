package com.example.Triple_clone.service.membership;

import com.example.Triple_clone.domain.entity.User;
import com.example.Triple_clone.dto.membership.UserJoinRequestDto;
import com.example.Triple_clone.dto.membership.UserResponseDto;
import com.example.Triple_clone.dto.membership.UserUpdateDto;
import com.example.Triple_clone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    public UserResponseDto join(UserJoinRequestDto userJoinRequestDto) {
        User user = userJoinRequestDto.toEntity();
        repository.save(user);
        return UserResponseDto.fromUser(user);
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
