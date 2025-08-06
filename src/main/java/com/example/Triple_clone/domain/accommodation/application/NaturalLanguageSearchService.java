package com.example.Triple_clone.domain.accommodation.application;

import com.example.Triple_clone.domain.accommodation.domain.AccommodationDocument;
import com.example.Triple_clone.domain.accommodation.domain.SearchKeywords;
import com.example.Triple_clone.domain.accommodation.domain.RegionKeywords;
import com.example.Triple_clone.domain.accommodation.domain.PriceRangeKeywords;
import com.example.Triple_clone.domain.accommodation.domain.SearchSuggestionKeywords;
import com.example.Triple_clone.domain.accommodation.infra.ESAccommodationRepositoryImpl;
import com.example.Triple_clone.domain.accommodation.web.dto.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class NaturalLanguageSearchService {

    private final SpellCheckService spellCheckService;
    private final ESAccommodationRepositoryImpl esRepository;
    private final SearchAnalyticsService searchAnalyticsService;

    // ========== 메인 검색 비즈니스 로직 ==========
    public SearchResponse executeNaturalLanguageSearch(String query, Pageable pageable, HttpServletRequest request) {
        Instant startTime = Instant.now();

        // 사용자 정보 추출
        String userAgent = request.getHeader("User-Agent");
        String clientIp = getClientIpAddress(request);
        log.info("자연어 검색 실행: query='{}', ip='{}', userAgent='{}'", query, clientIp, userAgent);

        // 자연어 파싱
        SearchParams params = parseAdvancedNaturalLanguage(query);
        params.setOriginalQuery(query);

        // ES 검색 실행
        Page<AccommodationDocument> searchResults = esRepository.advancedNaturalLanguageSearch(params, pageable);

        // 검색 제안 생성
        SearchSuggestionResponse suggestions = esRepository.getSearchSuggestions(query);

        // 응답 구성
        SearchResponse response = SearchResponse.builder()
                .results(searchResults)
                .searchParams(params)
                .suggestions(suggestions)
                .searchTime(Duration.between(startTime, Instant.now()).toMillis())
                .totalResults(searchResults.getTotalElements())
                .build();

        // 검색 분석 데이터 수집
        searchAnalyticsService.recordSearch(SearchEvent.builder()
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

        return response;
    }

    // ========== 자동완성 비즈니스 로직 ==========
    public List<AutocompleteResult> getSmartAutocomplete(String query, int limit) {
        if (query.length() < 2) {
            return List.of();
        }

        List<AutocompleteResult> results = esRepository.smartAutocomplete(query);

        // 결과를 타입별로 정렬
        return results.stream()
                .sorted((a, b) -> {
                    // 타입별 우선순위
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
    }

    // ========== 검색 제안 비즈니스 로직 ==========
    public SearchSuggestionResponse getSearchSuggestions(String query) {
        return esRepository.getSearchSuggestions(query);
    }

    // ========== 자연어 파싱 메서드 (완전 enum 기반) ==========
    public SearchParams parseAdvancedNaturalLanguage(String query) {
        String correctedQuery = spellCheckService.correctSpelling(query.toLowerCase());
        log.info("원본 쿼리: '{}' → 교정된 쿼리: '{}'", query, correctedQuery);

        SearchParams params = SearchParams.builder().build();
        List<String> searchKeywords = new ArrayList<>();

        params.setOriginalQuery(query);
        params.setCorrectedQuery(correctedQuery);

        // 위치 키워드 파싱 (enum 사용)
        Map<String, List<String>> locationKeywords = SearchKeywords.getLocationKeywords();
        for (Map.Entry<String, List<String>> entry : locationKeywords.entrySet()) {
            if (containsAnyKeyword(correctedQuery, entry.getValue())) {
                searchKeywords.add(entry.getKey());
                params.addLocationContext(entry.getKey());
            }
        }

        // 가격 범위 추출 (enum 사용)
        Integer priceMax = extractPriceRange(correctedQuery);
        if (priceMax != null) {
            if (PriceRangeKeywords.isBelowExpression(correctedQuery)) {
                params.setRoomPriceMax(priceMax);
            } else if (PriceRangeKeywords.isAboveExpression(correctedQuery)) {
                params.setRoomPriceMin(priceMax);
            } else {
                params.setRoomPriceMin((int)(priceMax * 0.8));
                params.setRoomPriceMax((int)(priceMax * 1.2));
            }
        }

        // 시간 관련 키워드 파싱 (enum 사용)
        Map<String, List<String>> timeKeywords = SearchKeywords.getTimeKeywords();
        if (timeKeywords.get("late_checkout") != null &&
                containsAnyKeyword(correctedQuery, timeKeywords.get("late_checkout"))) {
            params.setLateCheckout(true);
            searchKeywords.add("레이트체크아웃");
        }

        if (timeKeywords.get("early_checkin") != null &&
                containsAnyKeyword(correctedQuery, timeKeywords.get("early_checkin"))) {
            params.setEarlyCheckin(true);
            searchKeywords.add("얼리체크인");
        }

        // 시설 키워드 파싱 (enum 사용)
        Map<String, List<String>> facilityKeywords = SearchKeywords.getFacilityKeywords();
        for (Map.Entry<String, List<String>> entry : facilityKeywords.entrySet()) {
            if (containsAnyKeyword(correctedQuery, entry.getValue())) {
                searchKeywords.add(entry.getKey());
                params.addFacility(entry.getKey());
            }
        }

        // 할인 키워드 파싱 (enum 사용)
        Map<String, List<String>> discountKeywords = SearchKeywords.getDiscountKeywords();
        if (discountKeywords.get("discount") != null &&
                containsAnyKeyword(correctedQuery, discountKeywords.get("discount"))) {
            params.setHasDiscount(true);
            searchKeywords.add("할인");
        }

        // 카테고리 키워드 파싱 (enum 사용)
        Map<String, List<String>> categoryKeywords = SearchKeywords.getCategoryKeywords();
        for (Map.Entry<String, List<String>> entry : categoryKeywords.entrySet()) {
            if (containsAnyKeyword(correctedQuery, entry.getValue())) {
                params.setCategory(entry.getKey());
                break;
            }
        }

        // 수용인원 파싱 (enum 사용)
        Integer capacity = extractCapacity(correctedQuery);
        if (capacity != null) {
            params.setRoomCapacityMin(capacity);
        }

        // 객실 타입 파싱 (enum 사용)
        Map<String, List<String>> roomTypeKeywords = SearchKeywords.getRoomTypeKeywords();
        for (Map.Entry<String, List<String>> entry : roomTypeKeywords.entrySet()) {
            if (containsAnyKeyword(correctedQuery, entry.getValue())) {
                searchKeywords.add(entry.getValue().get(0)); // 첫 번째 키워드 사용
                params.addRoomType(entry.getKey());
            }
        }

        // 지역 파싱 (enum 사용)
        String region = RegionKeywords.extractRegion(correctedQuery);
        if (region != null) {
            params.setRegion(region);
            searchKeywords.add(region);
        }

        searchKeywords.add(correctedQuery);
        params.setSearchKeyword(String.join(" ", searchKeywords));
        params.setSuggestions(SearchSuggestionKeywords.generateSearchSuggestions(correctedQuery));

        log.info("파싱 결과: {}", params);
        return params;
    }

    // ========== Private 유틸리티 메서드들 ==========
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

    private Integer extractPriceRange(String query) {
        Pattern pricePattern = Pattern.compile("(\\d+)\\s*(만원?|원|k|천원?)");
        Matcher matcher = pricePattern.matcher(query);

        while (matcher.find()) {
            int number = Integer.parseInt(matcher.group(1));
            String unit = matcher.group(2);

            switch (unit) {
                case "만원", "만" -> { return number * 10000; }
                case "천원", "천" -> { return number * 1000; }
                case "k", "K" -> { return number * 1000; }
                default -> {
                    if (number > 1000) return number;
                }
            }
        }

        // enum 사용
        Map<String, List<String>> priceKeywords = SearchKeywords.getPriceKeywords();
        for (Map.Entry<String, List<String>> entry : priceKeywords.entrySet()) {
            if (containsAnyKeyword(query, entry.getValue())) {
                return Integer.valueOf(entry.getKey());
            }
        }

        return null;
    }

    private Integer extractCapacity(String query) {
        Pattern capacityPattern = Pattern.compile("(\\d+)\\s*(명|인|사람)");
        Matcher matcher = capacityPattern.matcher(query);

        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }

        // enum 사용
        Map<String, List<String>> capacityKeywords = SearchKeywords.getCapacityKeywords();
        for (Map.Entry<String, List<String>> entry : capacityKeywords.entrySet()) {
            if (containsAnyKeyword(query, entry.getValue())) {
                return Integer.valueOf(entry.getKey());
            }
        }

        return null;
    }

    private boolean containsAnyKeyword(String text, List<String> keywords) {
        return keywords.stream().anyMatch(text::contains);
    }
}