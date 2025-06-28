package com.example.Triple_clone.domain.plan;

import com.example.Triple_clone.domain.member.Member;
import com.example.Triple_clone.common.AuthErrorCode;
import com.example.Triple_clone.domain.member.UserService;
import com.example.Triple_clone.common.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PlanFacadeService {
    private final UserService userService;
    private final PlanService planService;

    private Member member;
    private Plan plan;

    public PlanCreateDto create(PlanCreateDto createDto, String email) {
        member = userService.findByEmail(email);
        Plan plan = createDto.toEntity(member);
        planService.save(plan);
        return createDto;
    }

    private void isExist(PlanDto dto, String email) {
        member = userService.findByEmail(email);
        plan = planService.findById(dto.planId());
    }

    public PlanReadResponseDto findPlan(PlanDto readRequestDto, String email) {
        isExist(readRequestDto, email);
        if (plan.isMine(member.getId())) {
            return new PlanReadResponseDto(plan);
        }

        throw new RestApiException(AuthErrorCode.AUTH_ERROR_CODE);
    }

    public PlanReadAllResponseDto findAllPlan(String email) {
        Member member = userService.findByEmail(email);
        List<PlanReadResponseDto> plans = new ArrayList<>();

        for (Plan plan : member.getPlans()) {
            plans.add(new PlanReadResponseDto(plan));
        }
        return new PlanReadAllResponseDto(plans);
    }

    public PlanStyleUpdateDto updateStyle(PlanStyleUpdateDto updateDto, String email) {
        Member member = userService.findByEmail(email);
        isExist(updateDto.planDto(), email);

        if (plan.isMine(member.getId())) {
            planService.updateStyle(updateDto, member.getId());
            return updateDto;
        }

        throw new RestApiException(AuthErrorCode.AUTH_ERROR_CODE);
    }

    public PlanPartnerUpdateDto updatePartner(PlanPartnerUpdateDto updateDto, String email) {
        Member member = userService.findByEmail(email);
        isExist(updateDto.planDto(), email);

        if (plan.isMine(member.getId())) {
            planService.updatePartner(updateDto, member.getId());
            return updateDto;
        }

        throw new RestApiException(AuthErrorCode.AUTH_ERROR_CODE);
    }

    public PlanDto deletePlan(PlanDto deleteDto, String email) {
        isExist(deleteDto, email);

        if (plan.isMine(member.getId())) {
            planService.delete(plan);
            return deleteDto;
        }

        throw new RestApiException(AuthErrorCode.AUTH_ERROR_CODE);
    }
}
