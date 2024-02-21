package com.example.Triple_clone.domain.entity;

import com.example.Triple_clone.domain.vo.Location;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class DetailPlanTest {
    @Test
    void 세부_계획_수정_테스트() {
        DetailPlan detailPlan = new DetailPlan();
        Location location = new Location(1D, 1D, "test");
        Date date = new Date(2024, Calendar.MARCH, 2);
        detailPlan.update(location, date, "test");

        assertThat(detailPlan.getLocation().getLatitude()).isEqualTo(1D);
        assertThat(detailPlan.getLocation().getLongitude()).isEqualTo(1D);
        assertThat(detailPlan.getLocation().getName()).isEqualTo("test");
        assertThat(detailPlan.getDate()).isEqualTo(date);
        assertThat(detailPlan.getTime()).isEqualTo("test");
    }
}
