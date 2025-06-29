package com.example.Triple_clone.domain.entity;

import com.example.Triple_clone.domain.accommodation.domain.Accommodation;
import com.example.Triple_clone.domain.plan.domain.Plan;
import com.example.Triple_clone.domain.plan.domain.Reservation;
import com.example.Triple_clone.domain.plan.domain.Location;
import org.junit.jupiter.api.Test;

import java.sql.Date;

import static org.assertj.core.api.Assertions.assertThat;

class ReservationTest {

    @Test
    void 예약_객체_생성_테스트() {
        // given
        Plan plan = new Plan();  // 단순한 목 객체 or 실제 Plan 객체 (기본 생성자 있으면 사용)
        Location location = new Location(1.0, 1.0, "서울");
        Date date = Date.valueOf("2025-04-23");
        String time = "15:00";

        Accommodation accommodation = Accommodation.builder()
                .local("서울")
                .name("트리플 호텔")
                .score(4.8)
                .category("호텔")
                .lentDiscountRate(10)
                .lentTime(2)
                .lentOriginPrice(100000)
                .lentPrice(90000)
                .lentStatus(true)
                .enterTime("15:00")
                .lodgmentDiscountRate(20)
                .lodgmentOriginPrice(200000)
                .lodgmentPrice(160000)
                .lodgmentStatus(true)
                .build();

        // when
        Reservation reservation = new Reservation(plan, location, date, time, accommodation);

        // then
        assertThat(reservation.getPlan()).isEqualTo(plan);
        assertThat(reservation.getLocation()).isEqualTo(location);
        assertThat(reservation.getDate()).isEqualTo(date);
        assertThat(reservation.getTime()).isEqualTo(time);
    }
}
