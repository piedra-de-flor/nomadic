package com.example.Triple_clone.service.planning;

import com.example.Triple_clone.domain.entity.Member;
import com.example.Triple_clone.domain.entity.Plan;
import com.example.Triple_clone.domain.vo.AuthErrorCode;
import com.example.Triple_clone.domain.vo.Partner;
import com.example.Triple_clone.domain.vo.Style;
import com.example.Triple_clone.dto.planning.*;
import com.example.Triple_clone.service.membership.UserService;
import com.example.Triple_clone.web.exception.RestApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlanFacadeServiceTest {

    private UserService userService;
    private PlanService planService;
    private PlanFacadeService facadeService;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        planService = mock(PlanService.class);
        facadeService = new PlanFacadeService(userService, planService);
    }

    @Test
    @DisplayName("플랜 생성 성공")
    void createPlan() {
        PlanCreateDto dto = new PlanCreateDto(1L, "2025-06-01", null, null, Partner.COUPLE.name(), List.of(Style.HEALING.name()));
        Member member = mock(Member.class);
        when(userService.findById(1L)).thenReturn(member);

        Plan plan = mock(Plan.class);
        when(dto.toEntity(member)).thenReturn(plan);

        PlanCreateDto result = facadeService.create(dto);

        verify(planService).save(plan);
        assertThat(result).isEqualTo(dto);
    }

    @Test
    @DisplayName("자신의 플랜 조회 성공")
    void findOwnPlan() {
        Member member = mock(Member.class);
        Plan plan = mock(Plan.class);
        PlanDto dto = new PlanDto(1L, 2L);

        when(userService.findById(1L)).thenReturn(member);
        when(planService.findById(2L)).thenReturn(plan);
        when(member.getId()).thenReturn(1L);
        when(plan.isMine(1L)).thenReturn(true);
        when(plan.getPlace()).thenReturn("testPlace");

        PlanReadResponseDto result = facadeService.findPlan(dto);

        assertThat(result.place()).isEqualTo(plan.getPlace());
    }

    @Test
    @DisplayName("타인의 플랜 조회 시 예외 발생")
    void findPlanNotMine() {
        Member member = mock(Member.class);
        Plan plan = mock(Plan.class);
        PlanDto dto = new PlanDto(1L, 2L);

        when(userService.findById(1L)).thenReturn(member);
        when(planService.findById(2L)).thenReturn(plan);
        when(member.getId()).thenReturn(1L);
        when(plan.isMine(1L)).thenReturn(false);

        assertThatThrownBy(() -> facadeService.findPlan(dto))
                .isInstanceOf(RestApiException.class);
    }

    @Test
    @DisplayName("유저의 모든 플랜 조회")
    void findAllPlans() {
        Member member = mock(Member.class);
        Plan plan1 = mock(Plan.class);
        Plan plan2 = mock(Plan.class);

        when(userService.findById(1L)).thenReturn(member);
        when(member.getPlans()).thenReturn(List.of(plan1, plan2));

        PlanReadAllResponseDto result = facadeService.findAllPlan(1L);

        assertThat(result.plans()).hasSize(2);
    }

    @Test
    @DisplayName("스타일 업데이트 성공")
    void updateStyleSuccess() {
        Member member = mock(Member.class);
        Plan plan = mock(Plan.class);
        PlanStyleUpdateDto updateDto = new PlanStyleUpdateDto(new PlanDto(1L, 2L), List.of(Style.HEALING.toString()));

        when(userService.findById(1L)).thenReturn(member);
        when(planService.findById(2L)).thenReturn(plan);
        when(member.getId()).thenReturn(1L);
        when(plan.isMine(1L)).thenReturn(true);

        PlanStyleUpdateDto result = facadeService.updateStyle(updateDto);

        verify(planService).updateStyle(updateDto);
        assertThat(result).isEqualTo(updateDto);
    }

    @Test
    @DisplayName("플랜 삭제 성공")
    void deletePlan() {
        Member member = mock(Member.class);
        Plan plan = mock(Plan.class);
        PlanDto dto = new PlanDto(1L, 2L);

        when(userService.findById(1L)).thenReturn(member);
        when(planService.findById(2L)).thenReturn(plan);
        when(member.getId()).thenReturn(1L);
        when(plan.isMine(1L)).thenReturn(true);

        PlanDto result = facadeService.deletePlan(dto);

        verify(planService).delete(plan);
        assertThat(result).isEqualTo(dto);
    }

    @Test
    @DisplayName("파트너 업데이트 성공")
    void updatePartnerSuccess() {
        Member member = mock(Member.class);
        Plan plan = mock(Plan.class);
        PlanPartnerUpdateDto dto = new PlanPartnerUpdateDto(new PlanDto(1L, 2L), "친구");

        when(userService.findById(1L)).thenReturn(member);
        when(planService.findById(2L)).thenReturn(plan);
        when(member.getId()).thenReturn(1L);
        when(plan.isMine(1L)).thenReturn(true);

        PlanPartnerUpdateDto result = facadeService.updatePartner(dto);

        verify(planService).updatePartner(dto);
        assertThat(result).isEqualTo(dto);
    }
}
