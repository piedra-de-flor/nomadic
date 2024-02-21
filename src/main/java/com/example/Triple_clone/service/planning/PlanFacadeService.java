package com.example.Triple_clone.service.planning;

import com.example.Triple_clone.domain.entity.Plan;
import com.example.Triple_clone.domain.entity.User;
import com.example.Triple_clone.domain.vo.AuthErrorCode;
import com.example.Triple_clone.dto.planning.*;
import com.example.Triple_clone.service.membership.UserService;
import com.example.Triple_clone.web.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PlanFacadeService {
    private final UserService userService;
    private final PlanService planService;

    private User user;
    private Plan plan;

    public PlanCreateDto create(PlanCreateDto createDto) {
        user = userService.findById(createDto.userId());
        Plan plan = createDto.toEntity(user);
        planService.save(plan);
        return createDto;
    }

    private void isExist(PlanDto dto) {
        user = userService.findById(dto.userId());
        plan = planService.findById(dto.planId());
    }

    public PlanReadResponseDto findPlan(PlanDto readRequestDto) {
        isExist(readRequestDto);
        if (plan.isMine(user.getId())) {
            return new PlanReadResponseDto(plan);
        }

        throw new RestApiException(AuthErrorCode.AUTH_ERROR_CODE);
    }

    public PlanReadAllResponseDto findAllPlan(long userId) {
        User user = userService.findById(userId);
        return new PlanReadAllResponseDto(user.getPlans());
    }

    public PlanStyleUpdateDto updateStyle(PlanStyleUpdateDto updateDto) {
        isExist(updateDto.planDto());

        if (plan.isMine(user.getId())) {
            planService.updateStyle(updateDto);
            return updateDto;
        }

        throw new RestApiException(AuthErrorCode.AUTH_ERROR_CODE);
    }

    public PlanPartnerUpdateDto updatePartner(PlanPartnerUpdateDto updateDto) {
        isExist(updateDto.planDto());

        if (plan.isMine(user.getId())) {
            planService.updatePartner(updateDto);
            return updateDto;
        }

        throw new RestApiException(AuthErrorCode.AUTH_ERROR_CODE);
    }

    public PlanDto deletePlan(PlanDto deleteDto) {
        isExist(deleteDto);

        if (plan.isMine(user.getId())) {
            planService.delete(plan);
            return deleteDto;
        }

        throw new RestApiException(AuthErrorCode.AUTH_ERROR_CODE);
    }
}
