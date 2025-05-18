package com.example.Triple_clone.domain.dto.planning;

import com.example.Triple_clone.dto.planning.PlanCreateDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class PlanCreateDtoTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void 계획_생성_DTO_유효성_성공() {
        PlanCreateDto dto = new PlanCreateDto(
                "test",
                new Date(),
                new Date(),
                "PARTNER",
                List.of("test")
        );

        Set<ConstraintViolation<PlanCreateDto>> violations = validator.validate(dto);
        assertThat(violations).isEmpty();
    }

    @Test
    void 계획_생성_DTO_유효성_실패_장소_null() {
        PlanCreateDto dto = new PlanCreateDto(
                null,
                new Date(),
                new Date(),
                "PARTNER",
                List.of("test")
        );

        Set<ConstraintViolation<PlanCreateDto>> violations = validator.validate(dto);
        assertThat(violations).hasSize(1);
    }

    @Test
    void 계획_생성_DTO_유효성_실패_시작날짜_null() {
        PlanCreateDto dto = new PlanCreateDto(
                "test",
                null,
                new Date(),
                "PARTNER",
                List.of("test")
        );

        Set<ConstraintViolation<PlanCreateDto>> violations = validator.validate(dto);
        assertThat(violations).hasSize(1);
    }

    @Test
    void 계획_생성_DTO_유효성_실패_끝날짜_null() {
        PlanCreateDto dto = new PlanCreateDto(
                "test",
                new Date(),
                null,
                "PARTNER",
                List.of("test")
        );

        Set<ConstraintViolation<PlanCreateDto>> violations = validator.validate(dto);
        assertThat(violations).hasSize(1);
    }
}
