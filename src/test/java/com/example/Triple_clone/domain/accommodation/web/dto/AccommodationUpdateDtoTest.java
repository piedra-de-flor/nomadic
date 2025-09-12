package com.example.Triple_clone.domain.accommodation.web.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class AccommodationUpdateDtoTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    @DisplayName("@Size 길이 제한 검증 (업데이트 DTO)")
    void 사이즈_검증() {
        AccommodationUpdateDto dto = AccommodationUpdateDto.builder()
                .name("a".repeat(256))
                .category("a".repeat(101))
                .grade("a".repeat(51))
                .region("a".repeat(101))
                .address("a".repeat(501))
                .landmarkDistance("a".repeat(201))
                .build();

        Set<ConstraintViolation<AccommodationUpdateDto>> violations = validator.validate(dto);

        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .contains(
                        "숙소명은 255자를 초과할 수 없습니다",
                        "카테고리는 100자를 초과할 수 없습니다",
                        "등급은 50자를 초과할 수 없습니다",
                        "지역은 100자를 초과할 수 없습니다",
                        "주소는 500자를 초과할 수 없습니다",
                        "랜드마크 거리는 200자를 초과할 수 없습니다"
                );
    }
}
