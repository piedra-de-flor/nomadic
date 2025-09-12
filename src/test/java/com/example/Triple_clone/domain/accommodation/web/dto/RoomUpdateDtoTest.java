package com.example.Triple_clone.domain.accommodation.web.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class RoomUpdateDtoTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    @DisplayName("@Min/@Size/@AssertTrue 검증")
    void 필드_검증() {
        RoomUpdateDto dto = RoomUpdateDto.builder()
                .name("a".repeat(256))
                .dayusePrice(-1)
                .dayuseSalePrice(-1)
                .dayuseTime("a".repeat(51))
                .stayPrice(-1)
                .staySalePrice(-1)
                .stayCheckinTime("a".repeat(51))
                .stayCheckoutTime("a".repeat(51))
                .capacity(3)
                .maxCapacity(2)
                .build();

        Set<ConstraintViolation<RoomUpdateDto>> violations = validator.validate(dto);

        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .contains(
                        "객실명은 255자를 초과할 수 없습니다",
                        "대실 가격은 0 이상이어야 합니다",
                        "대실 할인 가격은 0 이상이어야 합니다",
                        "대실 시간은 50자를 초과할 수 없습니다",
                        "숙박 가격은 0 이상이어야 합니다",
                        "숙박 할인 가격은 0 이상이어야 합니다",
                        "체크인 시간은 50자를 초과할 수 없습니다",
                        "체크아웃 시간은 50자를 초과할 수 없습니다",
                        "기본 수용 인원은 최대 수용 인원보다 클 수 없습니다"
                );
    }

    @Test
    @DisplayName("정상 값이면 위반 없음")
    void 정상값_검증() {
        RoomUpdateDto ok = RoomUpdateDto.builder()
                .name("슈페리어")
                .capacity(2)
                .maxCapacity(3)
                .stayPrice(120000)
                .build();

        assertThat(validator.validate(ok)).isEmpty();
    }
}
