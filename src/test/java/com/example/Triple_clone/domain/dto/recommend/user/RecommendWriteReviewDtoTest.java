package com.example.Triple_clone.domain.dto.recommend.user;

import com.example.Triple_clone.dto.recommend.user.RecommendWriteReviewDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class RecommendWriteReviewDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void 리뷰_작성_DTO_유효성_성공() {
        RecommendWriteReviewDto dto = new RecommendWriteReviewDto(
                1L, "test content", null);

        Set<ConstraintViolation<RecommendWriteReviewDto>> violations = validator.validate(dto);
        assertThat(violations).isEmpty();
    }

    @Test
    void 리뷰_작성_DTO_유효성_실패_내용_null() {
        RecommendWriteReviewDto dto = new RecommendWriteReviewDto(
                1L,  null, null);

        Set<ConstraintViolation<RecommendWriteReviewDto>> violations = validator.validate(dto);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("There is no contents");
    }

    @Test
    void 리뷰_작성_DTO_유효성_실패_내용_3000자_초과() {
        StringBuilder overContent = new StringBuilder();

        for (int i = 0; i < 31; i++) {
            overContent.append("....................................................................................................");
        }

        RecommendWriteReviewDto dto = new RecommendWriteReviewDto(
                1L,  String.valueOf(overContent), null);

        Set<ConstraintViolation<RecommendWriteReviewDto>> violations = validator.validate(dto);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("maximum contents size is 3000 words");
    }
}
