package com.example.Triple_clone.dto.membership;

import com.example.Triple_clone.domain.entity.User;

public record UserResponseDto(long userId, String name, String email, String role) {
    public static UserResponseDto fromUser(User user) {
        return new UserResponseDto(user.getId(), user.getName(), user.getEmail(), user.getRoles().toString());
    }
}
