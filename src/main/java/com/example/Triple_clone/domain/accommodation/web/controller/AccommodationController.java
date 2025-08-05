package com.example.Triple_clone.domain.accommodation.web.controller;

import com.example.Triple_clone.domain.accommodation.application.NaturalLanguageSearchService;
import com.example.Triple_clone.domain.accommodation.application.SearchAnalyticsService;
import com.example.Triple_clone.domain.accommodation.application.SpellCheckService;
import com.example.Triple_clone.domain.accommodation.domain.AccommodationDocument;
import com.example.Triple_clone.domain.accommodation.infra.ESAccommodationRepositoryImpl;
import com.example.Triple_clone.domain.accommodation.web.dto.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/accommodations")
@RequiredArgsConstructor
@Slf4j
public class AccommodationController {

    private final NaturalLanguageSearchService naturalLanguageService;
    private final SpellCheckService spellCheckService;
    private final ESAccommodationRepositoryImpl repository;
    private final SearchAnalyticsService analyticsService;

    // 1. 고도화된 자연어 검색 API
    @GetMapping("/natural-search")
    public ResponseEntity<SearchResponse> naturalLanguageSearch(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request
    ) {
        try {
            Instant startTime = Instant.now();

            // 검색 분석을 위한 로깅
            String userAgent = request.getHeader("User-Agent");
            String clientIp = getClientIpAddress(request);
            log.info("자연어 검색 요청: query='{}', ip='{}', userAgent='{}'", query, clientIp, userAgent);

            // 자연어 파싱
            SearchParams params = naturalLanguageService.parseAdvancedNaturalLanguage(query);
            params.setOriginalQuery(query);

            // 검색 실행
            Pageable pageable = PageRequest.of(page, size);
            Page<AccommodationDocument> searchResults = repository.advancedNaturalLanguageSearch(params, pageable);

            // 검색 제안 생성
            SearchSuggestionResponse suggestions = repository.getSearchSuggestions(query);

            // 응답 구성
            SearchResponse response = SearchResponse.builder()
                    .results(searchResults)
                    .searchParams(params)
                    .suggestions(suggestions)
                    .searchTime(Duration.between(startTime, Instant.now()).toMillis())
                    .totalResults(searchResults.getTotalElements())
                    .build();

            // 검색 분석 데이터 수집
            analyticsService.recordSearch(SearchEvent.builder()
                    .query(query)
                    .parsedParams(params)
                    .resultCount(searchResults.getTotalElements())
                    .searchTime(response.getSearchTime())
                    .userIp(clientIp)
                    .userAgent(userAgent)
                    .timestamp(Instant.now())
                    .build());

            log.info("자연어 검색 완료: query='{}', 결과={}건, 소요시간={}ms",
                    query, searchResults.getTotalElements(), response.getSearchTime());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("자연어 검색 오류: query='{}', error='{}'", query, e.getMessage(), e);
            return ResponseEntity.status(500).body(
                    SearchResponse.builder()
                            .error("검색 중 오류가 발생했습니다: " + e.getMessage())
                            .build()
            );
        }
    }

    // 2. 스마트 자동완성 API
    @GetMapping("/smart-autocomplete")
    public ResponseEntity<List<AutocompleteResult>> smartAutocomplete(
            @RequestParam String q,
            @RequestParam(defaultValue = "10") int limit
    ) {
        try {
            if (q.length() < 2) {
                return ResponseEntity.ok(Collections.emptyList());
            }

            List<AutocompleteResult> results = repository.smartAutocomplete(q);

            // 결과를 타입별로 그룹핑해서 정렬
            List<AutocompleteResult> sortedResults = results.stream()
                    .sorted((a, b) -> {
                        // 타입별 우선순위: exact > partial > region > room > corrected
                        Map<String, Integer> typeOrder = Map.of(
                                "exact", 1, "partial", 2, "region", 3, "room", 4, "corrected", 5
                        );
                        int orderA = typeOrder.getOrDefault(a.getType(), 6);
                        int orderB = typeOrder.getOrDefault(b.getType(), 6);

                        if (orderA != orderB) {
                            return Integer.compare(orderA, orderB);
                        }
                        return Double.compare(b.getScore(), a.getScore());
                    })
                    .limit(limit)
                    .collect(Collectors.toList());

            log.debug("자동완성 요청: q='{}', 결과={}건", q, sortedResults.size());
            return ResponseEntity.ok(sortedResults);

        } catch (Exception e) {
            log.error("자동완성 오류: q='{}', error='{}'", q, e.getMessage(), e);
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    // 3. 검색 제안 API
    @GetMapping("/search-suggestions")
    public ResponseEntity<SearchSuggestionResponse> getSearchSuggestions(
            @RequestParam String query
    ) {
        try {
            SearchSuggestionResponse suggestions = repository.getSearchSuggestions(query);
            return ResponseEntity.ok(suggestions);
        } catch (Exception e) {
            log.error("검색 제안 오류: query='{}', error='{}'", query, e.getMessage(), e);
            return ResponseEntity.ok(new SearchSuggestionResponse());
        }
    }

    // 4. 실시간 트렌딩 검색어 API
    @GetMapping("/trending")
    public ResponseEntity<List<TrendingSearch>> getTrendingSearches() {
        try {
            List<TrendingSearch> trending = repository.getTrendingSearches();
            return ResponseEntity.ok(trending);
        } catch (Exception e) {
            log.error("트렌딩 검색어 조회 오류: {}", e.getMessage(), e);
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    // 5. 검색 통계 API (관리자용)
    @GetMapping("/admin/search-analytics")
    public ResponseEntity<SearchAnalyticsResponse> getSearchAnalytics(
            @RequestParam(defaultValue = "7") int days
    ) {
        try {
            SearchAnalyticsResponse analytics = analyticsService.getSearchAnalytics(days);
            return ResponseEntity.ok(analytics);
        } catch (Exception e) {
            log.error("검색 통계 조회 오류: {}", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }

    // 6. 검색어 오타 교정 API
    @GetMapping("/spell-check")
    public ResponseEntity<SpellCheckResponse> checkSpelling(
            @RequestParam String query
    ) {
        try {
            String corrected = spellCheckService.correctSpelling(query);
            boolean hasCorrectionl = !query.equals(corrected);

            SpellCheckResponse response = SpellCheckResponse.builder()
                    .originalQuery(query)
                    .correctedQuery(corrected)
                    .hasCorrectionl(hasCorrectionl)
                    .build();

            if (hasCorrectionl) {
                // 교정된 쿼리로 추천 검색어 생성
                List<String> suggestions = generateSpellSuggestions(corrected);
                response.setSuggestions(suggestions);
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("오타 교정 오류: query='{}', error='{}'", query, e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }

    // 7. 개인화된 검색 추천 API
    @GetMapping("/personalized-suggestions")
    public ResponseEntity<List<String>> getPersonalizedSuggestions(
            HttpServletRequest request,
            @RequestParam(required = false) Long userId
    ) {
        try {
            String clientIp = getClientIpAddress(request);

            // 사용자 검색 히스토리 기반 추천 (간단한 버전)
            List<String> recentSearches = analyticsService.getRecentSearchesByUser(userId, clientIp);
            List<String> personalizedSuggestions = generatePersonalizedSuggestions(recentSearches);

            return ResponseEntity.ok(personalizedSuggestions);

        } catch (Exception e) {
            log.error("개인화 추천 오류: {}", e.getMessage(), e);
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    // 유틸리티 메서드들
    private String getClientIpAddress(HttpServletRequest request) {
        String clientIp = request.getHeader("X-Forwarded-For");
        if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("X-Real-IP");
        }
        if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getRemoteAddr();
        }
        return clientIp;
    }

    private List<String> generateSpellSuggestions(String correctedQuery) {
        // 교정된 쿼리를 기반으로 검색 제안 생성
        return Arrays.asList(
                correctedQuery + " 숙소",
                correctedQuery + " 호텔",
                correctedQuery + " 펜션",
                correctedQuery + " 근처"
        );
    }

    private List<String> generatePersonalizedSuggestions(List<String> recentSearches) {
        Set<String> suggestions = new LinkedHashSet<>();

        for (String search : recentSearches) {
            // 최근 검색어 기반 확장 제안
            suggestions.add(search + " 주변");
            suggestions.add(search + " 할인");
            suggestions.add(search + " 리뷰 좋은");
        }

        return suggestions.stream().limit(10).collect(Collectors.toList());
    }
}
