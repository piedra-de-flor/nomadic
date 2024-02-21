package com.example.Triple_clone.domain.dto.recommend.admin;

import com.example.Triple_clone.dto.recommend.admin.AdminRecommendCreatePlaceDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class AdminRecommendCreatePlaceDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void 추천_장소_생성_DTO_유효성_성공() {
        AdminRecommendCreatePlaceDto dto = new AdminRecommendCreatePlaceDto(
                "Title", "http://example.com", "SubTitle", "Location", "main_image");

        Set<ConstraintViolation<AdminRecommendCreatePlaceDto>> violations = validator.validate(dto);
        assertThat(violations).isEmpty();
    }

    @Test
    void 추천_장소_생성_DTO_유효성_실패_제목_null() {
        AdminRecommendCreatePlaceDto dto = new AdminRecommendCreatePlaceDto(
                null, "http://example.com", "SubTitle", "Location", "main_image");

        Set<ConstraintViolation<AdminRecommendCreatePlaceDto>> violations = validator.validate(dto);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Title can not be null");
    }

    @Test
    void 추천_장소_생성_DTO_유효성_실패_노션_null() {
        AdminRecommendCreatePlaceDto dto = new AdminRecommendCreatePlaceDto(
                "Title", null, "SubTitle", "Location", "main_image");

        Set<ConstraintViolation<AdminRecommendCreatePlaceDto>> violations = validator.validate(dto);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Notion URL can not be null");
    }

    @Test
    void 추천_장소_생성_DTO_유효성_실패_위치_null() {
        AdminRecommendCreatePlaceDto dto = new AdminRecommendCreatePlaceDto(
                "Title", "http://example.com", "SubTitle", null, "main_image");

        Set<ConstraintViolation<AdminRecommendCreatePlaceDto>> violations = validator.validate(dto);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Location can not be null");
    }
}

