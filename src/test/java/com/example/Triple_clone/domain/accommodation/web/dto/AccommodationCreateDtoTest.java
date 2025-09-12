package com.example.Triple_clone.domain.accommodation.web.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class AccommodationCreateDtoTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    @DisplayName("@NotBlank 필수 필드 검증")
    void 필수필드_검증() {
        AccommodationCreateDto dto = AccommodationCreateDto.builder()
                .name("")
                .region("")
                .address("")
                .build();

        Set<ConstraintViolation<AccommodationCreateDto>> violations = validator.validate(dto);

        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .contains("숙소명은 필수입니다", "지역은 필수입니다", "주소는 필수입니다");
    }

    @Test
    @DisplayName("@Size 길이 제한 검증")
    void 사이즈_검증() {
        String long256 = "a".repeat(256);
        String long101 = "a".repeat(101);
        String long51  = "a".repeat(51);
        String long501 = "a".repeat(501);
        String long201 = "a".repeat(201);

        AccommodationCreateDto dto = AccommodationCreateDto.builder()
                .name(long256)
                .category(long101)
                .grade(long51)
                .region(long101)
                .address(long501)
                .landmarkDistance(long201)
                .build();

        Set<ConstraintViolation<AccommodationCreateDto>> violations = validator.validate(dto);

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
