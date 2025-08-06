package com.example.Triple_clone.domain.accommodation.domain;

import java.util.Arrays;
import java.util.List;

public enum PriceRangeKeywords {

    // 가격 범위 표현
    BELOW("이하", "이하", "아래", "미만", "까지", "대 이하", "원 이하"),
    ABOVE("이상", "이상", "위", "초과", "부터", "대 이상", "원 이상");

    private final String key;
    private final List<String> synonyms;

    PriceRangeKeywords(String key, String... synonyms) {
        this.key = key;
        this.synonyms = Arrays.asList(synonyms);
    }

    public String getKey() {
        return key;
    }

    public List<String> getSynonyms() {
        return synonyms;
    }

    // 특정 텍스트가 "이하" 표현인지 확인
    public static boolean isBelowExpression(String text) {
        return BELOW.getSynonyms().stream().anyMatch(text::contains);
    }

    // 특정 텍스트가 "이상" 표현인지 확인
    public static boolean isAboveExpression(String text) {
        return ABOVE.getSynonyms().stream().anyMatch(text::contains);
    }
}