package com.example.Triple_clone.domain.dto.planning;

import com.example.Triple_clone.domain.vo.Location;
import com.example.Triple_clone.dto.planning.DetailPlanDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class DetailPlanDtoTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void 세부_계획_생성_DTO_유효성_성공() {
        DetailPlanDto dto = new DetailPlanDto(1L, new Location(1D,1D, "Test Location"), new Date(), "test");

        Set<ConstraintViolation<DetailPlanDto>> violations = validator.validate(dto);

        assertThat(violations).isEmpty();
    }

    @Test
    void 세부_계획_생성_DTO_유효성_실패_위치_null() {
        DetailPlanDto dto = new DetailPlanDto(1L, null, new Date(), "test");

        Set<ConstraintViolation<DetailPlanDto>> violations = validator.validate(dto);

        assertThat(violations).hasSize(1);
    }

    @Test
    void 세부_계획_생성_DTO_유효성_실패_날짜_null() {
        DetailPlanDto dto = new DetailPlanDto(1L, new Location(1D,1D, "Test Location"), null, "test");

        Set<ConstraintViolation<DetailPlanDto>> violations = validator.validate(dto);

        assertThat(violations).hasSize(1);
    }
}
