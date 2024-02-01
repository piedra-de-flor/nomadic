package com.example.Triple_clone.service.planning;

import com.example.Triple_clone.domain.entity.DetailPlan;
import com.example.Triple_clone.domain.entity.Plan;
import com.example.Triple_clone.domain.entity.User;
import com.example.Triple_clone.domain.vo.Location;
import com.example.Triple_clone.dto.planning.PlanCreateDto;
import com.example.Triple_clone.dto.planning.PlanDto;
import com.example.Triple_clone.dto.planning.PlanPartnerUpdateDto;
import com.example.Triple_clone.dto.planning.PlanStyleUpdateDto;
import com.example.Triple_clone.repository.PlanRepository;
import com.example.Triple_clone.service.membership.UserService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlanFacadeServiceTest {
    @InjectMocks
    PlanFacadeService planFacadeService;
    @Mock
    PlanService planService;
    @Mock
    UserService userService;
    @Mock
    Plan plan;
    @Mock
    User user;

    @Test
    void 계획_생성_테스트(){
        PlanCreateDto planCreateDto = mock(PlanCreateDto.class);
        when(planCreateDto.userId()).thenReturn(1L);
        when(planCreateDto.toEntity(user)).thenReturn(plan);
        when(userService.findById(anyLong())).thenReturn(user);

        planFacadeService.create(planCreateDto);

        verify(planService, times(1)).save(plan);
    }
}
