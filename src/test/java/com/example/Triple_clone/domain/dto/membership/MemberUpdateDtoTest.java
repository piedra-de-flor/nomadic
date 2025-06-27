package com.example.Triple_clone.domain.dto.membership;

import com.example.Triple_clone.domain.member.UserUpdateDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberUpdateDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void 유저_업데이트_DTO_유효성_성공() {
        UserUpdateDto dto = new UserUpdateDto(1L, "test", "password123");

        Set<ConstraintViolation<UserUpdateDto>> violations = validator.validate(dto);
        assertThat(violations).isEmpty();
    }

    @Test
    void 유저_업데이트_DTO_유효성_실패_이름_null() {
        UserUpdateDto dto = new UserUpdateDto(1L, null, "password123");

        Set<ConstraintViolation<UserUpdateDto>> violations = validator.validate(dto);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Name can not be null");
    }

    @Test
    void 유저_업데이트_DTO_유효성_실패_비밀번호_길이제한_미달() {
        UserUpdateDto dto = new UserUpdateDto(1L, "test", "123");

        Set<ConstraintViolation<UserUpdateDto>> violations = validator.validate(dto);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("password size must be between 4 and 13");
    }

    @Test
    void 유저_업데이트_DTO_유효성_실패_비밀번호_길이제한_초과() {
        UserUpdateDto dto = new UserUpdateDto(1L, "test", "12345678910111213");

        Set<ConstraintViolation<UserUpdateDto>> violations = validator.validate(dto);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("password size must be between 4 and 13");
    }
}
