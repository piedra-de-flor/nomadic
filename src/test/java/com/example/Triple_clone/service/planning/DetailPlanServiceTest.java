package com.example.Triple_clone.service.planning;

import com.example.Triple_clone.domain.plan.DetailPlan;
import com.example.Triple_clone.domain.plan.DetailPlanService;
import com.example.Triple_clone.domain.plan.Location;
import com.example.Triple_clone.domain.plan.DetailPlanUpdateDto;
import com.example.Triple_clone.domain.plan.DetailPlanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.util.Optional;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class DetailPlanServiceTest {

    private DetailPlanRepository repository;
    private DetailPlanService service;

    @BeforeEach
    void setUp() {
        repository = mock(DetailPlanRepository.class);
        service = new DetailPlanService(repository);
    }

    @Test
    @DisplayName("ID로 상세 일정 조회 성공")
    void findByIdSuccess() {
        DetailPlan detailPlan = mock(DetailPlan.class);
        when(repository.findById(1L)).thenReturn(Optional.of(detailPlan));

        DetailPlan result = service.findById(1L);

        assertThat(result).isEqualTo(detailPlan);
    }

    @Test
    @DisplayName("ID로 상세 일정 조회 실패 시 예외 발생")
    void findByIdFail() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(1L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("new plan Entity");
    }

    @Test
    @DisplayName("상세 일정 저장")
    void saveDetailPlan() {
        DetailPlan detailPlan = mock(DetailPlan.class);

        service.save(detailPlan);

        verify(repository).save(detailPlan);
    }

    @Test
    @DisplayName("상세 일정 삭제")
    void deleteDetailPlan() {
        DetailPlan detailPlan = mock(DetailPlan.class);

        service.delete(detailPlan);

        verify(repository).delete(detailPlan);
    }

    @Test
    @DisplayName("상세 일정 수정")
    void updateDetailPlan() {
        DetailPlan detailPlan = mock(DetailPlan.class);
        DetailPlanUpdateDto dto = new DetailPlanUpdateDto(1L, 1L, new Location(0.0,0.0,"서울"), Date.valueOf("2025-06-01"), "13:00");

        service.update(detailPlan, dto);

        verify(detailPlan).update(dto.location(), dto.date(), dto.time());
    }
}
