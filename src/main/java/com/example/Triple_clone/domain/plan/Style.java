package com.example.Triple_clone.domain.plan;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public enum Style {
    HEALING("healing"),
    ACTIVITY("activity");

    private final String style;

    public static List<Style> toStyles(List<String> input) {
        List<Style> styles = new ArrayList<>();
        input.forEach(s -> styles.add(Style.valueOf(s)));
        return styles;
    }
}
