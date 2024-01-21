package com.example.Triple_clone.domain.vo;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Partner {
    ALONE("alone"),
    COUPLE("couple"),
    FAMILY("family");

    private final String partner;
}
