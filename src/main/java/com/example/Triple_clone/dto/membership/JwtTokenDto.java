package com.example.Triple_clone.dto.membership;

import lombok.Builder;

@Builder
public record JwtTokenDto(
        String grantType,
        String accessToken,
        String refreshToken
) {
}
