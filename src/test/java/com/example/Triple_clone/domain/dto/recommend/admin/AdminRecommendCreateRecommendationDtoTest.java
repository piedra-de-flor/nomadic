package com.example.Triple_clone.domain.dto.recommend.admin;

import com.example.Triple_clone.dto.recommend.admin.AdminRecommendCreateRecommendationDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class AdminRecommendCreateRecommendationDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void 추천_장소_생성_DTO_유효성_성공() {
        AdminRecommendCreateRecommendationDto dto = new AdminRecommendCreateRecommendationDto(
                "Title", "http://example.com", "SubTitle", "Location", "main_image");

        Set<ConstraintViolation<AdminRecommendCreateRecommendationDto>> violations = validator.validate(dto);
        assertThat(violations).isEmpty();
    }

    @Test
    void 추천_장소_생성_DTO_유효성_실패_제목_null() {
        AdminRecommendCreateRecommendationDto dto = new AdminRecommendCreateRecommendationDto(
                null, "http://example.com", "SubTitle", "Location", "main_image");

        Set<ConstraintViolation<AdminRecommendCreateRecommendationDto>> violations = validator.validate(dto);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Title can not be null");
    }

    @Test
    void 추천_장소_생성_DTO_유효성_실패_노션_null() {
        AdminRecommendCreateRecommendationDto dto = new AdminRecommendCreateRecommendationDto(
                "Title", null, "SubTitle", "Location", "main_image");

        Set<ConstraintViolation<AdminRecommendCreateRecommendationDto>> violations = validator.validate(dto);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Notion URL can not be null");
    }

    @Test
    void 추천_장소_생성_DTO_유효성_실패_위치_null() {
        AdminRecommendCreateRecommendationDto dto = new AdminRecommendCreateRecommendationDto(
                "Title", "http://example.com", "SubTitle", null, "main_image");

        Set<ConstraintViolation<AdminRecommendCreateRecommendationDto>> violations = validator.validate(dto);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Location can not be null");
    }
}

