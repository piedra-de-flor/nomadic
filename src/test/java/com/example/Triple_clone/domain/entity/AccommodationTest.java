package com.example.Triple_clone.domain.entity;

import com.example.Triple_clone.domain.accommodation.domain.Accommodation;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class AccommodationTest {

    @Test
    void 숙소_객체_빌더_테스트() {
        // given
        String local = "서울";
        String name = "트리플 하우스";
        double score = 4.5;
        String category = "호텔";
        long lentDiscountRate = 15;
        int lentTime = 3;
        long lentOriginPrice = 100000;
        long lentPrice = 85000;
        boolean lentStatus = true;
        String enterTimeStr = "15:00";
        LocalTime enterTime = LocalTime.parse(enterTimeStr);
        long lodgmentDiscountRate = 10;
        long lodgmentOriginPrice = 200000;
        long lodgmentPrice = 180000;
        boolean lodgmentStatus = true;

        // when
        Accommodation accommodation = Accommodation.builder()
                .local(local)
                .name(name)
                .score(score)
                .category(category)
                .lentDiscountRate(lentDiscountRate)
                .lentTime(lentTime)
                .lentOriginPrice(lentOriginPrice)
                .lentPrice(lentPrice)
                .lentStatus(lentStatus)
                .enterTime(enterTimeStr)
                .lodgmentDiscountRate(lodgmentDiscountRate)
                .lodgmentOriginPrice(lodgmentOriginPrice)
                .lodgmentPrice(lodgmentPrice)
                .lodgmentStatus(lodgmentStatus)
                .build();

        // then
        assertThat(accommodation.getLocal()).isEqualTo(local);
        assertThat(accommodation.getName()).isEqualTo(name);
        assertThat(accommodation.getScore()).isEqualTo(score);
        assertThat(accommodation.getCategory()).isEqualTo(category);
        assertThat(accommodation.getLentDiscountRate()).isEqualTo(lentDiscountRate);
        assertThat(accommodation.getLentTime()).isEqualTo(lentTime);
        assertThat(accommodation.getLentOriginPrice()).isEqualTo(lentOriginPrice);
        assertThat(accommodation.getLentPrice()).isEqualTo(lentPrice);
        assertThat(accommodation.isLentStatus()).isTrue();
        assertThat(accommodation.getEnterTime()).isEqualTo(enterTime);
        assertThat(accommodation.getLodgmentDiscountRate()).isEqualTo(lodgmentDiscountRate);
        assertThat(accommodation.getLodgmentOriginPrice()).isEqualTo(lodgmentOriginPrice);
        assertThat(accommodation.getLodgmentPrice()).isEqualTo(lodgmentPrice);
        assertThat(accommodation.isLodgmentStatus()).isTrue();
    }
}
