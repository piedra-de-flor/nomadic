package com.example.Triple_clone.dto.membership;

import com.example.Triple_clone.domain.entity.User;
import com.example.Triple_clone.domain.vo.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

public record UserJoinRequestDto(
        @NotBlank(message = "Name can not be null")
        String name,
        @NotBlank(message = "Email can not be null")
        @Email
        String email,
        @NotBlank(message = "Password can not be null")
        @Size(min = 4, max = 13, message = "password size must be between 4 and 13")
        String password,
        String role)
{
    @Builder
    public UserJoinRequestDto(String name, String email, String password, String role) {
        if (role == null) {
            role = "USER";
        }
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User toEntity() {
        return User.builder()
                .name(name)
                .email(email)
                .password(password)
                .role(Role.valueOf(role))
                .build();
    }
}
