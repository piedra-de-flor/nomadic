package com.example.Triple_clone.dto.planning;

import com.example.Triple_clone.domain.entity.Plan;
import com.example.Triple_clone.domain.entity.User;
import com.example.Triple_clone.domain.vo.Partner;
import com.example.Triple_clone.domain.vo.Style;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;
import java.util.List;

public record PlanCreateDto(
        long userId,
        @NotBlank(message = "Place can not be null")
        String place,
        @NotBlank(message = "Start day can not be null")
        Date startDay,
        @NotBlank(message = "End day can not be null")
        Date endDay,
        String partner,
        List<String> styles
) {
    public Plan toEntity(User user) {
        return Plan.builder()
                .user(user)
                .place(place)
                .styles(Style.toStyles(styles))
                .startDay(startDay)
                .endDay(endDay)
                .partner(Partner.valueOf(partner))
                .build();
    }
}
