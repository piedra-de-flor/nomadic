package com.example.Triple_clone.service.planning;

import com.example.Triple_clone.domain.plan.domain.DetailPlan;
import com.example.Triple_clone.domain.plan.domain.Plan;
import com.example.Triple_clone.domain.plan.application.PlanService;
import com.example.Triple_clone.domain.plan.domain.Location;
import com.example.Triple_clone.domain.plan.web.dto.plan.PlanDto;
import com.example.Triple_clone.domain.plan.web.dto.plan.PlanPartnerUpdateDto;
import com.example.Triple_clone.domain.plan.web.dto.plan.PlanStyleUpdateDto;
import com.example.Triple_clone.domain.plan.infra.PlanRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlanServiceTest {
    @InjectMocks
    PlanService planService;
    @Mock
    PlanRepository repository;
    @Mock
    Plan plan;

    @Test
    void 계획_조회_성공_테스트(){
        when(repository.findById(anyLong())).thenReturn(Optional.ofNullable(plan));

        Plan result = planService.findById(1L);

        assertNotNull(result);
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void 계획_조회_실패_테스트(){
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class, () -> planService.findById(1L));
    }

    @Test
    void 세부_계획_조회_성공_테스트(){;
        when(repository.findById(anyLong())).thenReturn(Optional.ofNullable(plan));

        List<DetailPlan> result = planService.getPlans(1L);

        assertNotNull(result);
        verify(plan, times(1)).getPlans();
    }

    @Test
    void 계획_저장_테스트(){
        planService.save(plan);
        verify(repository, times(1)).save(plan);
    }

    @Test
    void 계획_삭제_테스트(){
        planService.delete(plan);
        verify(repository, times(1)).delete(plan);
    }

    @Test
    void 계획_스타일_수정_성공_테스트(){
        long userId = 1L;
        PlanStyleUpdateDto planStyleUpdateDto = mock(PlanStyleUpdateDto.class);
        PlanDto planDto = mock(PlanDto.class);

        when(repository.findById(anyLong())).thenReturn(Optional.ofNullable(plan));
        when(planStyleUpdateDto.planDto()).thenReturn(planDto);
        when(planStyleUpdateDto.planDto().planId()).thenReturn(1L);
        when(plan.isMine(anyLong())).thenReturn(true);
        when(planDto.planId()).thenReturn(1L);

        planService.updateStyle(planStyleUpdateDto);

        verify(plan, times(1)).chooseStyle(any());
    }

    @Test
    void 계획_스타일_수정_실패_계획_소유_없음_테스트(){
        long userId = 1L;
        PlanStyleUpdateDto planStyleUpdateDto = mock(PlanStyleUpdateDto.class);
        PlanDto planDto = mock(PlanDto.class);

        when(repository.findById(anyLong())).thenReturn(Optional.ofNullable(plan));
        when(plan.isMine(1L)).thenReturn(false);
        when(planStyleUpdateDto.planDto()).thenReturn(planDto);
        when(planDto.planId()).thenReturn(1L);

        Assertions.assertThrows(IllegalArgumentException.class, () -> planService.updateStyle(planStyleUpdateDto));
    }

    @Test
    void 계획_스타일_수정_실패_계획_없음_테스트(){
        long userId = 1L;
        PlanStyleUpdateDto planStyleUpdateDto = mock(PlanStyleUpdateDto.class);
        PlanDto planDto = mock(PlanDto.class);

        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        when(planStyleUpdateDto.planDto()).thenReturn(planDto);
        when(planDto.planId()).thenReturn(1L);

        Assertions.assertThrows(NoSuchElementException.class, () -> planService.updateStyle(planStyleUpdateDto));
    }

    @Test
    void 계획_파트너_수정_성공_테스트(){
        long userId = 1L;
        PlanPartnerUpdateDto planPartnerUpdateDto = mock(PlanPartnerUpdateDto.class);
        PlanDto planDto = mock(PlanDto.class);

        when(repository.findById(anyLong())).thenReturn(Optional.ofNullable(plan));
        when(planPartnerUpdateDto.planDto()).thenReturn(planDto);
        when(planDto.planId()).thenReturn(1L);
        when(plan.isMine(anyLong())).thenReturn(true);
        when(planPartnerUpdateDto.partner()).thenReturn("COUPLE");

        planService.updatePartner(planPartnerUpdateDto);

        verify(plan, times(1)).choosePartner(any());
    }

    @Test
    void 계획_파트너_수정_실패_계획_소유_없음_테스트(){
        long userId = 1L;
        PlanPartnerUpdateDto planPartnerUpdateDto = mock(PlanPartnerUpdateDto.class);
        PlanDto planDto = mock(PlanDto.class);

        when(repository.findById(anyLong())).thenReturn(Optional.ofNullable(plan));
        when(plan.isMine(1L)).thenReturn(false);
        when(planPartnerUpdateDto.planDto()).thenReturn(planDto);
        when(planDto.planId()).thenReturn(1L);

        Assertions.assertThrows(IllegalArgumentException.class, () -> planService.updatePartner(planPartnerUpdateDto));
    }

    @Test
    void 계획_파트너_수정_실패_계획_없음_테스트(){
        long userId = 1L;
        PlanPartnerUpdateDto planPartnerUpdateDto = mock(PlanPartnerUpdateDto.class);
        PlanDto planDto = mock(PlanDto.class);

        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        when(planPartnerUpdateDto.planDto()).thenReturn(planDto);
        when(planDto.planId()).thenReturn(1L);

        Assertions.assertThrows(NoSuchElementException.class, () -> planService.updatePartner(planPartnerUpdateDto));
    }

    @Test
    void 계획_위치_추가_성공_테스트() {;
        when(repository.findById(anyLong())).thenReturn(Optional.ofNullable(plan));
        when(plan.getPlans()).thenReturn(new ArrayList<>());

        List<Location> locations = planService.addLocation(1L, "test", 1D, 1D);

        assertEquals(locations.get(0).getName(),  "test");
        assertEquals(locations.get(0).getLatitude(),  1D);
        assertEquals(locations.get(0).getLongitude(),  1D);
    }

    @Test
    void 계획_위치_리스트_조회_성공_테스트() {
        long planId = 1L;
        List<DetailPlan> mockDetailPlans = new ArrayList<>();
        when(repository.findById(anyLong())).thenReturn(Optional.ofNullable(plan));
        when(planService.getPlans(planId)).thenReturn(mockDetailPlans);

        List<Location> result = planService.getLocation(planId);

        assertNotNull(result);
        assertEquals(mockDetailPlans.size(), result.size());
        for (int i = 0; i < mockDetailPlans.size(); i++) {
            Location expectedLocation = mockDetailPlans.get(i).getLocation();
            Location actualLocation = result.get(i);
            assertEquals(expectedLocation, actualLocation);
        }
    }
}
