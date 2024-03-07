package com.example.Triple_clone.domain.vo;

import com.querydsl.core.types.dsl.NumberPath;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.example.Triple_clone.domain.entity.QAccommodation.accommodation;

@Getter
@RequiredArgsConstructor
public enum QueryDslPriceConditions {
    LENT(accommodation.lentPrice),
    LODGE(accommodation.totalPrice);

    private final NumberPath<Long> condition;
}
