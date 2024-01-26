package com.example.Triple_clone.service.planning;

import com.example.Triple_clone.domain.entity.DetailPlan;
import com.example.Triple_clone.domain.entity.Plan;
import com.example.Triple_clone.domain.entity.User;
import com.example.Triple_clone.domain.vo.AuthErrorCode;
import com.example.Triple_clone.domain.vo.Partner;
import com.example.Triple_clone.domain.vo.Style;
import com.example.Triple_clone.dto.planning.*;
import com.example.Triple_clone.repository.PlanRepository;
import com.example.Triple_clone.repository.UserRepository;
import com.example.Triple_clone.web.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PlanService {
    private final PlanRepository planRepository;
    private final UserRepository userRepository;
    private User user;
    private Plan plan;

    public PlanCreateDto createPlan(PlanCreateDto createDto) {
        user = userRepository.findById(createDto.userId())
                .orElseThrow(() -> new NoSuchElementException("no user Entity"));
        Plan plan = createDto.toEntity(user);
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

        throw new RestApiException(AuthErrorCode.AUTH_ERROR_CODE);
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

        throw new RestApiException(AuthErrorCode.AUTH_ERROR_CODE);
    }

    public PlanPartnerUpdateDto updatePartner(PlanPartnerUpdateDto updateDto) {
        isExist(updateDto.planDto());

        if (plan.isMine(user.getId())) {
            plan.choosePartner(Partner.valueOf(updateDto.partner()));
            return updateDto;
        }

        throw new RestApiException(AuthErrorCode.AUTH_ERROR_CODE);
    }

    public PlanDto deletePlan(PlanDto deleteDto) {
        isExist(deleteDto);

        if (plan.isMine(user.getId())) {
            planRepository.delete(plan);
            return deleteDto;
        }

        throw new RestApiException(AuthErrorCode.AUTH_ERROR_CODE);
    }

    public List<DetailPlan> getPlans(long planId) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new NoSuchElementException("new plan Entity"));

        return plan.getPlans();
    }
}