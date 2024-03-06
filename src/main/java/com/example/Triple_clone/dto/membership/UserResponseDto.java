package com.example.Triple_clone.dto.membership;

import com.example.Triple_clone.domain.entity.Member;

public record UserResponseDto(long userId, String name, String email, String role) {
    public static UserResponseDto fromUser(Member member) {
        return new UserResponseDto(member.getId(), member.getName(), member.getEmail(), member.getRoles().get(0));
    }
}
