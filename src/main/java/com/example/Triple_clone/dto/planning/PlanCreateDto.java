package com.example.Triple_clone.dto.planning;

import com.example.Triple_clone.domain.entity.Plan;
import com.example.Triple_clone.domain.entity.User;
import com.example.Triple_clone.domain.vo.Partner;
import com.example.Triple_clone.domain.vo.Style;

import java.util.Date;
import java.util.List;

public record PlanCreateDto(
        long userId,
        String place,
        Date startDay,
        Date endDay,
        String partner,
        List<String> styles
) {
    public Plan toEntity(User user) {
        return Plan.builder()
                .userId(user.getId())
                .place(place)
                .styles(Style.toStyles(styles))
                .startDay(startDay)
                .endDay(endDay)
                .partner(Partner.valueOf(partner))
                .build();
    }
}
