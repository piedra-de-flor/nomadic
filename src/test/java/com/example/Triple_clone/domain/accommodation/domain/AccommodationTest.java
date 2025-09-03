package com.example.Triple_clone.domain.accommodation.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class AccommodationTest {

    @Test
    @DisplayName("숙소 엔티티 - 업데이트 시 null 아닌 필드만 반영")
    void 숙소_업데이트_부분반영() {
        Accommodation acc = Accommodation.builder()
                .name("원래이름").category("호텔").grade("4성")
                .region("서울").address("주소").intro("소개")
                .build();

        acc.update(null, "수정이름", null, "5성", "부산",
                null, "지하철 5분", null, "편의시설", null);

        assertThat(acc.getName()).isEqualTo("수정이름");
        assertThat(acc.getGrade()).isEqualTo("5성");
        assertThat(acc.getRegion()).isEqualTo("부산");
        assertThat(acc.getAddress()).isEqualTo("주소");
        assertThat(acc.getAmenities()).isEqualTo("편의시설");
    }

    @Test
    @DisplayName("숙소 엔티티 - 객실 추가/삭제/소유 검증")
    void 숙소_객실_추가삭제() {
        Accommodation acc = Accommodation.builder().name("호텔").region("서울").address("주소").build();

        Room r1 = Room.builder().name("디럭스").capacity(2).maxCapacity(3).build();
        Room r2 = Room.builder().name("디럭스").capacity(2).maxCapacity(3).build();

        acc.addRoom(r1);
        acc.addRoom(r2);

        assertThat(acc.getRooms()).hasSize(1);
        assertThat(acc.containsRoom(r1)).isTrue();

        acc.removeRoom(r1);
        assertThat(acc.getRooms()).isEmpty();
    }
}
