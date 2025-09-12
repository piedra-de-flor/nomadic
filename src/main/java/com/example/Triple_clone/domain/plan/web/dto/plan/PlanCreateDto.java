package com.example.Triple_clone.domain.plan.web.dto.plan;

import com.example.Triple_clone.domain.member.domain.Member;
import com.example.Triple_clone.domain.plan.domain.Style;
import com.example.Triple_clone.domain.plan.domain.Partner;
import com.example.Triple_clone.domain.plan.domain.Plan;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;

public record PlanCreateDto(
        @NotBlank(message = "Place can not be null")
        String place,
        @NotNull(message = "Start day can not be null")
        Date startDay,
        @NotNull(message = "End day can not be null")
        Date endDay,
        String partner,
        List<String> styles
) {
    public Plan toEntity(Member member) {
        return Plan.builder()
                .member(member)
                .place(place)
                .styles(Style.toStyles(styles))
                .startDay(startDay)
                .endDay(endDay)
                .partner(Partner.valueOf(partner))
                .build();
    }
}
