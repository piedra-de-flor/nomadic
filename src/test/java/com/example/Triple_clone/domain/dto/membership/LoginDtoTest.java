package com.example.Triple_clone.domain.dto.membership;

import com.example.Triple_clone.dto.membership.LoginDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void 로그인_DTO_유효성_성공() {
        LoginDto loginDto = new LoginDto("example@example.com", "password123");
        Set<ConstraintViolation<LoginDto>> violations = validator.validate(loginDto);
        assertThat(violations).isEmpty();
    }

    @Test
    void 로그인_DTO_유효성_실패_이메일_null() {
        LoginDto loginDto = new LoginDto(null, "password123");
        Set<ConstraintViolation<LoginDto>> violations = validator.validate(loginDto);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Email can not be null");
    }

    @Test
    void 로그인_DTO_유효성_실패_이메일_형식_X() {
        LoginDto loginDto = new LoginDto("invalid-email", "password123");
        Set<ConstraintViolation<LoginDto>> violations = validator.validate(loginDto);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("올바른 형식의 이메일 주소여야 합니다");
    }

    @Test
    void 로그인_DTO_유효성_실패_비밀번호_null() {
        LoginDto loginDto = new LoginDto("example@example.com", null);
        Set<ConstraintViolation<LoginDto>> violations = validator.validate(loginDto);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Password can not be null");
    }

    @Test
    void 로그인_DTO_유효성_실패_비밀번호_길이제한_미달() {
        LoginDto loginDto = new LoginDto("example@example.com", "123");
        Set<ConstraintViolation<LoginDto>> violations = validator.validate(loginDto);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("password size must be between 4 and 13");
    }

    @Test
    void 로그인_DTO_유효성_실패_비밀번호_길이제한_초과() {
        LoginDto loginDto = new LoginDto("example@example.com", "1234567891011121314");
        Set<ConstraintViolation<LoginDto>> violations = validator.validate(loginDto);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("password size must be between 4 and 13");
    }
}
