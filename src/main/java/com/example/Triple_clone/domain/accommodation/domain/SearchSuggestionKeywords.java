package com.example.Triple_clone.domain.accommodation.domain;

import java.util.Arrays;
import java.util.List;

public enum SearchSuggestionKeywords {

    // 검색 제안 템플릿
    OCEAN_SUGGESTIONS("바다", Arrays.asList(
            "바다뷰 오션뷰 해변 숙소",
            "해변가 근처 호텔"
    )),

    DISCOUNT_SUGGESTIONS("할인", Arrays.asList(
            "특가 할인 프로모션 숙소",
            "세일 중인 호텔"
    )),

    FAMILY_SUGGESTIONS("가족", Arrays.asList(
            "가족 단위 4인실 숙소",
            "어린이 동반 가능한 호텔"
    )),

    COUPLE_SUGGESTIONS("커플", Arrays.asList(
            "커플 전용 펜션",
            "로맨틱 호텔 스위트룸"
    )),

    POOL_SUGGESTIONS("수영장", Arrays.asList(
            "수영장 있는 리조트",
            "풀빌라 독채"
    )),

    // 오타 교정 후 검색 제안 템플릿
    SPELL_CHECK_SUFFIX_ACCOMMODATION("숙소"),
    SPELL_CHECK_SUFFIX_HOTEL("호텔"),
    SPELL_CHECK_SUFFIX_PENSION("펜션"),
    SPELL_CHECK_SUFFIX_NEARBY("근처"),

    // 개인화 추천 템플릿
    PERSONALIZED_SUFFIX_NEARBY("주변"),
    PERSONALIZED_SUFFIX_DISCOUNT("할인"),
    PERSONALIZED_SUFFIX_GOOD_REVIEW("리뷰 좋은");

    private final String triggerKeyword;
    private final List<String> suggestions;

    // 검색 제안용 생성자
    SearchSuggestionKeywords(String triggerKeyword, List<String> suggestions) {
        this.triggerKeyword = triggerKeyword;
        this.suggestions = suggestions;
    }

    // 단순 키워드용 생성자 (suffix 등)
    SearchSuggestionKeywords(String keyword) {
        this.triggerKeyword = keyword;
        this.suggestions = List.of(keyword);
    }

    public String getTriggerKeyword() {
        return triggerKeyword;
    }

    public List<String> getSuggestions() {
        return suggestions;
    }

    // 특정 쿼리에 대한 검색 제안 생성
    public static List<String> generateSearchSuggestions(String query) {
        for (SearchSuggestionKeywords suggestion : values()) {
            if (suggestion.suggestions.size() > 1 && // 실제 제안이 있는 경우만
                    query.contains(suggestion.getTriggerKeyword())) {
                return suggestion.getSuggestions();
            }
        }
        return List.of();
    }

    // 오타 교정 후 제안 생성
    public static List<String> generateSpellCheckSuggestions(String correctedQuery) {
        return List.of(
                correctedQuery + " " + SPELL_CHECK_SUFFIX_ACCOMMODATION.getTriggerKeyword(),
                correctedQuery + " " + SPELL_CHECK_SUFFIX_HOTEL.getTriggerKeyword(),
                correctedQuery + " " + SPELL_CHECK_SUFFIX_PENSION.getTriggerKeyword(),
                correctedQuery + " " + SPELL_CHECK_SUFFIX_NEARBY.getTriggerKeyword()
        );
    }

    // 개인화 추천 생성
    public static List<String> generatePersonalizedSuggestions(List<String> recentSearches) {
        return recentSearches.stream()
                .flatMap(search -> List.of(
                        search + " " + PERSONALIZED_SUFFIX_NEARBY.getTriggerKeyword(),
                        search + " " + PERSONALIZED_SUFFIX_DISCOUNT.getTriggerKeyword(),
                        search + " " + PERSONALIZED_SUFFIX_GOOD_REVIEW.getTriggerKeyword()
                ).stream())
                .limit(10)
                .toList();
    }
}