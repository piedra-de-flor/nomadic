package com.example.Triple_clone.domain.vo;

import com.querydsl.core.types.dsl.StringPath;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.example.Triple_clone.domain.entity.QAccommodation.accommodation;

@Getter
@RequiredArgsConstructor
public enum QueryDslStringConditions {
    LOCAL(accommodation.local),
    NAME(accommodation.name),
    CATEGORY(accommodation.category);

    private final StringPath condition;
}
