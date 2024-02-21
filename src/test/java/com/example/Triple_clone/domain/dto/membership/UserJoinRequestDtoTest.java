package com.example.Triple_clone.domain.dto.membership;

import com.example.Triple_clone.dto.membership.UserJoinRequestDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class UserJoinRequestDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void 회원가입_DTO_유효성_성공() {
        UserJoinRequestDto dto = UserJoinRequestDto.builder()
                .name("test")
                .email("test@test.com")
                .password("password123")
                .role("USER")
                .build();
        Set<ConstraintViolation<UserJoinRequestDto>> violations = validator.validate(dto);
        assertThat(violations).isEmpty();
    }

    @Test
    void 회원가입_DTO_유효성_실패_이름_null() {
        UserJoinRequestDto dto = UserJoinRequestDto.builder()
                .email("test@test.com")
                .password("password123")
                .role("USER")
                .build();
        Set<ConstraintViolation<UserJoinRequestDto>> violations = validator.validate(dto);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Name can not be null");
    }

    @Test
    void 회원가입_DTO_유효성_실패_이메일_null() {
        UserJoinRequestDto dto = UserJoinRequestDto.builder()
                .name("test")
                .password("password123")
                .role("USER")
                .build();
        Set<ConstraintViolation<UserJoinRequestDto>> violations = validator.validate(dto);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Email can not be null");
    }

    @Test
    void 회원가입_DTO_유효성_실패_이메일_형식_X() {
        UserJoinRequestDto dto = UserJoinRequestDto.builder()
                .name("test")
                .email("invalid-email")
                .password("password123")
                .role("USER")
                .build();
        Set<ConstraintViolation<UserJoinRequestDto>> violations = validator.validate(dto);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("올바른 형식의 이메일 주소여야 합니다");
    }

    @Test
    void 회원가입_DTO_유효성_실패_비밀번호_null() {
        UserJoinRequestDto dto = UserJoinRequestDto.builder()
                .name("test")
                .email("test@test.com")
                .role("USER")
                .build();
        Set<ConstraintViolation<UserJoinRequestDto>> violations = validator.validate(dto);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Password can not be null");
    }

    @Test
    void 회원가입_DTO_유효성_실패_비밀번호_길이제한_미달() {
        UserJoinRequestDto dto = UserJoinRequestDto.builder()
                .name("test")
                .email("test@test.com")
                .password("123")
                .role("USER")
                .build();
        Set<ConstraintViolation<UserJoinRequestDto>> violations = validator.validate(dto);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("password size must be between 4 and 13");
    }

    @Test
    void 회원가입_DTO_유효성_실패_비밀번호_길이제한_초과() {
        UserJoinRequestDto dto = UserJoinRequestDto.builder()
                .name("test")
                .email("test@test.com")
                .password("12345678910111213")
                .role("USER")
                .build();
        Set<ConstraintViolation<UserJoinRequestDto>> violations = validator.validate(dto);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("password size must be between 4 and 13");
    }
}

