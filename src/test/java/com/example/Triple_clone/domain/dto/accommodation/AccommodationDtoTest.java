package com.example.Triple_clone.domain.dto.accommodation;

import com.example.Triple_clone.domain.accommodation.Accommodation;
import com.example.Triple_clone.domain.accommodation.AccommodationDocument;
import com.example.Triple_clone.domain.accommodation.AccommodationDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AccommodationDtoTest {

    @Test
    @DisplayName("Accommodation -> AccommodationDto 변환 테스트")
    void testFromAccommodationEntity() {
        Accommodation accommodation = Accommodation.builder()
                .local("서울")
                .name("테스트 숙소")
                .score(4.5)
                .category("호텔")
                .lentDiscountRate(20L)
                .lentTime(5)
                .lentOriginPrice(100000L)
                .lentPrice(80000L)
                .lentStatus(true)
                .enterTime("15:00")
                .lodgmentDiscountRate(15L)
                .lodgmentOriginPrice(200000L)
                .lodgmentPrice(170000L)
                .lodgmentStatus(true)
                .build();

        AccommodationDto dto = new AccommodationDto(accommodation);

        assertThat(dto.local()).isEqualTo("서울");
        assertThat(dto.name()).isEqualTo("테스트 숙소");
        assertThat(dto.score()).isEqualTo(4.5);
        assertThat(dto.category()).isEqualTo("호텔");
        assertThat(dto.lentDiscountRate()).isEqualTo(20L);
        assertThat(dto.lentTime()).isEqualTo(5);
        assertThat(dto.lentOriginPrice()).isEqualTo(100000L);
        assertThat(dto.lentPrice()).isEqualTo(80000L);
        assertThat(dto.lentStatus()).isTrue();
        assertThat(dto.enterTime()).isEqualTo("15:00");
        assertThat(dto.lodgmentDiscountRate()).isEqualTo(15L);
        assertThat(dto.lodgmentOriginPrice()).isEqualTo(200000L);
        assertThat(dto.lodgmentPrice()).isEqualTo(170000L);
        assertThat(dto.lodgmentStatus()).isTrue();
    }

    @Test
    @DisplayName("AccommodationDocument -> AccommodationDto 변환 테스트 (null-safe)")
    void testFromAccommodationDocumentWithNulls() {
        AccommodationDocument document = new AccommodationDocument();
        document.setLocal("부산");
        document.setName("문서 숙소");
        document.setCategory("모텔");

        AccommodationDto dto = new AccommodationDto(document);

        assertThat(dto.local()).isEqualTo("부산");
        assertThat(dto.name()).isEqualTo("문서 숙소");
        assertThat(dto.score()).isEqualTo(0.0);
        assertThat(dto.category()).isEqualTo("모텔");
        assertThat(dto.lentDiscountRate()).isEqualTo(0L);
        assertThat(dto.lentTime()).isEqualTo(0);
        assertThat(dto.lentOriginPrice()).isEqualTo(0L);
        assertThat(dto.lentPrice()).isEqualTo(0L);
        assertThat(dto.lentStatus()).isFalse();
        assertThat(dto.enterTime()).isEqualTo("");
        assertThat(dto.lodgmentDiscountRate()).isEqualTo(0L);
        assertThat(dto.lodgmentOriginPrice()).isEqualTo(0L);
        assertThat(dto.lodgmentPrice()).isEqualTo(0L);
        assertThat(dto.lodgmentStatus()).isFalse();
    }

    @Test
    @DisplayName("AccommodationDto -> Accommodation 변환 테스트")
    void testToEntity() {
        AccommodationDto dto = new AccommodationDto(
                "제주", "리조트 숙소", 4.0, "리조트",
                30L, 3, 300000L, 210000L, true, "14:00",
                10L, 250000L, 225000L, true
        );

        Accommodation entity = dto.toEntity();

        assertThat(entity.getLocal()).isEqualTo("제주");
        assertThat(entity.getName()).isEqualTo("리조트 숙소");
        assertThat(entity.getScore()).isEqualTo(4.0);
        assertThat(entity.getCategory()).isEqualTo("리조트");
        assertThat(entity.getLentDiscountRate()).isEqualTo(30L);
        assertThat(entity.getLentTime()).isEqualTo(3);
        assertThat(entity.getLentOriginPrice()).isEqualTo(300000L);
        assertThat(entity.getLentPrice()).isEqualTo(210000L);
        assertThat(entity.isLentStatus()).isTrue();
        assertThat(entity.getEnterTime()).isEqualTo("14:00");
        assertThat(entity.getLodgmentDiscountRate()).isEqualTo(10L);
        assertThat(entity.getLodgmentOriginPrice()).isEqualTo(250000L);
        assertThat(entity.getLodgmentPrice()).isEqualTo(225000L);
        assertThat(entity.isLodgmentStatus()).isTrue();
    }
}
