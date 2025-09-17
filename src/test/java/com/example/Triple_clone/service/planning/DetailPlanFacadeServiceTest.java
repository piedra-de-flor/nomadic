package com.example.Triple_clone.service.planning;

import com.example.Triple_clone.domain.accommodation.domain.Accommodation;
import com.example.Triple_clone.domain.member.domain.Member;
import com.example.Triple_clone.domain.plan.application.DetailPlanFacadeService;
import com.example.Triple_clone.domain.plan.application.DetailPlanService;
import com.example.Triple_clone.domain.plan.application.PlanService;
import com.example.Triple_clone.domain.plan.domain.DetailPlan;
import com.example.Triple_clone.domain.plan.domain.Plan;
import com.example.Triple_clone.domain.plan.web.dto.detailplan.DetailPlanDto;
import com.example.Triple_clone.domain.plan.web.dto.detailplan.DetailPlanUpdateDto;
import com.example.Triple_clone.domain.plan.web.dto.ReservationCreateDto;
import com.example.Triple_clone.domain.recommend.domain.Recommendation;
import com.example.Triple_clone.domain.plan.domain.Location;
import com.example.Triple_clone.domain.accommodation.application.AccommodationService;
import com.example.Triple_clone.domain.recommend.application.RecommendQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class DetailPlanFacadeServiceTest {

    private PlanService planService;
    private DetailPlanService detailPlanService;
    private RecommendQueryService recommendQueryService;
    private AccommodationService accommodationService;
    private DetailPlanFacadeService facadeService;

    @BeforeEach
    void setUp() {
        planService = mock(PlanService.class);
        detailPlanService = mock(DetailPlanService.class);
        recommendQueryService = mock(RecommendQueryService.class);
        accommodationService = mock(AccommodationService.class);
        facadeService = new DetailPlanFacadeService(planService, detailPlanService, recommendQueryService, accommodationService);
    }

    @Test
    @DisplayName("상세 일정 생성 성공")
    void createDetailPlan() {
    }

    @Test
    @DisplayName("추천 장소 추가로 상세 일정 생성")
    void addRecommendation() {
        // given
        Member member = mock(Member.class);
        when(member.getId()).thenReturn(1L);

        Plan plan = Plan.builder()
                .member(member)
                .startDay(Date.valueOf("2025-05-01"))
                .build();

        Recommendation recommendation = Recommendation.builder()
                .title("추천 장소")
                .location(new Location(0.0, 0.0, "부산"))
                .build();

        when(planService.findById(1L)).thenReturn(plan);
        when(recommendQueryService.findById(2L)).thenReturn(recommendation);

        // when
        DetailPlanDto result = facadeService.addRecommendation(2L, 1L);

        // then
        assertThat(result.planId()).isEqualTo(1L);
        assertThat(result.location().getName()).isEqualTo("부산");
    }

    @Test
    @DisplayName("숙소 예약 상세 일정 생성")
    void createReservation() {
        Member member = mock(Member.class);
        when(member.getId()).thenReturn(1L);
        Plan plan = Plan.builder().member(member).build();
        Accommodation accommodation = mock(Accommodation.class);
        ReservationCreateDto dto = mock(ReservationCreateDto.class);
        DetailPlan detailPlan = mock(DetailPlan.class);

        when(planService.findById(anyLong())).thenReturn(plan);
        when(accommodationService.findById(anyLong())).thenReturn(accommodation);
        when(dto.toEntity(plan, accommodation)).thenReturn(detailPlan);

        ReservationCreateDto result = facadeService.createReservation(dto);

        verify(detailPlanService).save(detailPlan);
        assertThat(result).isEqualTo(dto);
    }

    @Test
    @DisplayName("상세 일정 수정 성공")
    void updateDetailPlan() {
    }

    @Test
    @DisplayName("본인의 일정이 아닌 경우 수정 시 예외 발생")
    void updateDetailPlanFail_NotOwned() {
        Member member = mock(Member.class);
        when(member.getId()).thenReturn(1L);
        Plan plan = Plan.builder().member(member).build();
        DetailPlan detailPlan = mock(DetailPlan.class);
        DetailPlanUpdateDto updateDto = new DetailPlanUpdateDto(1L, 1L, new Location(0.0, 0.0, "부산"), Date.valueOf("2025-07-01"), "11:00");

        when(planService.findById(1L)).thenReturn(plan);
        when(detailPlanService.findById(1L)).thenReturn(detailPlan);
        when(planService.getPlans(1L)).thenReturn(List.of()); // 포함 안됨

        assertThatThrownBy(() -> facadeService.update(updateDto))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("this detail plan id is not yours");
    }

    @Test
    @DisplayName("상세 일정 삭제 성공")
    void deleteDetailPlan() {
    }
}