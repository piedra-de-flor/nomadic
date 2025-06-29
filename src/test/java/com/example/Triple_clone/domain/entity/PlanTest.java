package com.example.Triple_clone.domain.entity;

import com.example.Triple_clone.domain.member.domain.Member;
import com.example.Triple_clone.domain.plan.domain.Plan;
import com.example.Triple_clone.domain.plan.domain.Partner;
import com.example.Triple_clone.domain.plan.domain.Style;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlanTest {
    @Mock
    Member member;

    @Test
    void 계획_파트너_선택_성공_테스트() {
        Plan plan = new Plan();
        plan.choosePartner(Partner.ALONE);

        assertThat(plan.getPartner()).isEqualTo(Partner.ALONE);
    }

    @Test
    void 계획_파트너_선택_실패_테스트() {
        Plan plan = new Plan();

        Assertions.assertThrows(IllegalArgumentException.class, () -> plan.choosePartner(Partner.valueOf("test")));
    }

    @Test
    void 계획_여행_스타일_선택_성공_테스트() {
        Plan plan = new Plan();
        plan.chooseStyle(List.of(Style.HEALING));

        assertThat(plan.getStyles()).isEqualTo(List.of(Style.HEALING));
    }

    @Test
    void 계획_여행_스타일_선택_실패_테스트() {
        Plan plan = new Plan();

        Assertions.assertThrows(IllegalArgumentException.class, () -> plan.chooseStyle(List.of(Style.valueOf("test"))));
    }

    @Test
    void 회원의_계획_소유_여부_검증_성공_테스트() {
        when(member.getId()).thenReturn(1L);
        Plan plan = Plan.builder()
                .place("test")
                .member(member)
                .partner(null)
                .styles(null)
                .startDay(new Date())
                .endDay(new Date())
                .build();

        assertThat(plan.isMine(1L)).isEqualTo(true);
    }

    @Test
    void 회원의_계획_소유_여부_검증_실패_테스트() {
        when(member.getId()).thenReturn(1L);
        Plan plan = Plan.builder()
                .place("test")
                .member(member)
                .partner(null)
                .styles(null)
                .startDay(new Date())
                .endDay(new Date())
                .build();

        assertThat(plan.isMine(2L)).isEqualTo(false);
    }
}
