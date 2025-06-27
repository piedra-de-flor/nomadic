package com.example.Triple_clone.domain.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserUpdateDto(
        long userId,
        @NotBlank(message = "Name can not be null")
        String name,
        @NotBlank(message = "Password can not be null")
        @Size(min = 4, max = 13, message = "password size must be between 4 and 13")
        String password) {
}
