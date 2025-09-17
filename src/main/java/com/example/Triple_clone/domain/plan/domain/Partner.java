package com.example.Triple_clone.domain.plan.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Partner {
    ALONE("alone"),
    COUPLE("couple"),
    FAMILY("family"),
    FRIEND("friend"),
    BUSINESS("business"),
    CLUB("club");

    private final String partner;
}
