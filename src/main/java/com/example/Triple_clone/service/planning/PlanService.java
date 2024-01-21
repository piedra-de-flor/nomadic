package com.example.Triple_clone.service.planning;

import com.example.Triple_clone.domain.entity.Plan;
import com.example.Triple_clone.domain.entity.User;
import com.example.Triple_clone.domain.vo.Partner;
import com.example.Triple_clone.domain.vo.Style;
import com.example.Triple_clone.dto.planning.*;
import com.example.Triple_clone.repository.PlanRepository;
import com.example.Triple_clone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PlanService {
    private final PlanRepository planRepository;
    private final UserRepository userRepository;
    private User user;
    private Plan plan;

    public PlanCreateDto createPlan(PlanCreateDto createDto) {
        Plan plan = createDto.toEntity();
        planRepository.save(plan);
        return createDto;
    }

    private void isExist(PlanDto dto) {
        user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new NoSuchElementException("no user Entity"));
        plan = planRepository.findById(dto.planId())
                .orElseThrow(() -> new NoSuchElementException("no plan Entity"));
    }

    public PlanReadResponseDto findPlan(PlanDto readRequestDto) {
        isExist(readRequestDto);
        if (plan.isMine(user.getId())) {
            return new PlanReadResponseDto(plan);
        }

        throw new IllegalArgumentException("no auth to access this plan id");
    }

    public PlanReadAllResponseDto findAllPlan(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("no user Entity"));

        return new PlanReadAllResponseDto(user.getPlans());
    }

    public PlanStyleUpdateDto updateStyle(PlanStyleUpdateDto updateDto) {
        isExist(updateDto.planDto());

        if (plan.isMine(user.getId())) {
            plan.chooseStyle(Style.toStyles(updateDto.styles()));
            return updateDto;
        }

        throw new IllegalArgumentException("no auth to access this plan id");
    }

    public PlanPartnerUpdateDto updatePartner(PlanPartnerUpdateDto updateDto) {
        isExist(updateDto.planDto());

        if (plan.isMine(user.getId())) {
            plan.choosePartner(Partner.valueOf(updateDto.partner()));
            return updateDto;
        }

        throw new IllegalArgumentException("no auth to access this plan id");
    }

    public PlanDto deletePlan(PlanDto deleteDto) {
        isExist(deleteDto);

        if (plan.isMine(user.getId())) {
            planRepository.delete(plan);
            return deleteDto;
        }

        throw new IllegalArgumentException("no auth to access this plan id");
    }
}