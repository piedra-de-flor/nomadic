package com.example.Triple_clone.domain.accommodation.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class SpellCheckService {
    private static final Map<String, String> COMMON_TYPOS = createTyposMap();

    private static Map<String, String> createTyposMap() {
        Map<String, String> typos = new HashMap<>();

        addAccommodationTypos(typos);
        addViewTypos(typos);
        addDiscountTypos(typos);
        addFacilityTypos(typos);
        addPriceTypos(typos);

        return Collections.unmodifiableMap(typos);
    }

    private static void addAccommodationTypos(Map<String, String> typos) {
        typos.put("호털", "호텔");
        typos.put("호테르", "호텔");
        typos.put("호탤", "호텔");
        typos.put("펜숙", "펜션");
        typos.put("펜숀", "펜션");
        typos.put("모털", "모텔");
    }

    private static void addViewTypos(Map<String, String> typos) {
        typos.put("바닷뷰", "바다뷰");
        typos.put("오씬뷰", "오션뷰");
        typos.put("씨뷰", "바다뷰");
    }

    private static void addDiscountTypos(Map<String, String> typos) {
        typos.put("할일", "할인");
        typos.put("하인", "할인");
        typos.put("세일", "세일");
    }

    private static void addFacilityTypos(Map<String, String> typos) {
        typos.put("수영창", "수영장");
        typos.put("수용장", "수영장");
        typos.put("조씩", "조식");
        typos.put("주착", "주차");
        typos.put("와이하이", "와이파이");
    }

    private static void addPriceTypos(Map<String, String> typos) {
        typos.put("10만", "10만원");
        typos.put("십만", "10만원");
        typos.put("100k", "100000");
        typos.put("5만", "5만원");
        typos.put("50k", "50000");
    }

    public String correctSpelling(String input) {
        String corrected = input;
        for (Map.Entry<String, String> entry : COMMON_TYPOS.entrySet()) {
            corrected = corrected.replaceAll(entry.getKey(), entry.getValue());
        }

        log.debug("오타 교정: '{}' → '{}'", input, corrected);
        return corrected;
    }

    // 레벤슈타인 거리 기반 유사도 검사
    public List<String> getSuggestions(String input, List<String> candidates) {
        return candidates.stream()
                .filter(candidate -> levenshteinDistance(input, candidate) <= 2)
                .sorted((a, b) -> Integer.compare(
                        levenshteinDistance(input, a),
                        levenshteinDistance(input, b)
                ))
                .limit(5)
                .collect(Collectors.toList());
    }

    private int levenshteinDistance(String a, String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];

        for (int i = 0; i <= a.length(); i++) {
            for (int j = 0; j <= b.length(); j++) {
                if (i == 0) dp[i][j] = j;
                else if (j == 0) dp[i][j] = i;
                else {
                    dp[i][j] = Math.min(
                            Math.min(dp[i-1][j] + 1, dp[i][j-1] + 1),
                            dp[i-1][j-1] + (a.charAt(i-1) == b.charAt(j-1) ? 0 : 1)
                    );
                }
            }
        }
        return dp[a.length()][b.length()];
    }
}
