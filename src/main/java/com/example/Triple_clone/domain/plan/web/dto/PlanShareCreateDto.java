package com.example.Triple_clone.domain.plan.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PlanShareCreateDto(
        @NotNull(message = "Plan ID cannot be null")
        Long planId,

        @NotBlank(message = "Shared member email cannot be blank")
        @Email(message = "Invalid email format")
        String sharedMemberEmail,

        @NotBlank(message = "Role cannot be blank")
        String role
) {
}