package com.example.Triple_clone.domain.accommodation.application;

import com.example.Triple_clone.domain.accommodation.domain.SearchSuggestionKeywords;
import com.example.Triple_clone.domain.accommodation.infra.ESAccommodationRepositoryImpl;
import com.example.Triple_clone.domain.accommodation.web.dto.SearchAnalyticsResponse;
import com.example.Triple_clone.domain.accommodation.web.dto.SearchEvent;
import com.example.Triple_clone.domain.accommodation.web.dto.TrendingSearch;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SearchAnalyticsService {

    private final ESAccommodationRepositoryImpl esRepository;
    private final List<SearchEvent> searchHistory = new CopyOnWriteArrayList<>();

    // ========== 검색 이벤트 기록 ==========
    public void recordSearch(SearchEvent event) {
        searchHistory.add(event);

        if (searchHistory.size() > 10000) {
            searchHistory.subList(0, searchHistory.size() - 10000).clear();
        }

        log.debug("검색 이벤트 기록: {}", event);
    }

    // ========== 검색 통계 조회 ==========
    public SearchAnalyticsResponse getSearchAnalytics(int days) {
        Instant cutoff = Instant.now().minus(days, ChronoUnit.DAYS);

        List<SearchEvent> recentEvents = searchHistory.stream()
                .filter(event -> event.getTimestamp().isAfter(cutoff))
                .collect(Collectors.toList());

        return SearchAnalyticsResponse.builder()
                .totalSearches((long) recentEvents.size())
                .averageSearchTime(recentEvents.stream()
                        .mapToLong(SearchEvent::getSearchTime)
                        .average()
                        .orElse(0.0))
                .topQueries(getTopQueries(recentEvents))
                .topCorrectedQueries(getTopCorrectedQueries(recentEvents))
                .searchesByHour(getSearchesByHour(recentEvents))
                .searchesByCategory(getSearchesByCategory(recentEvents))
                .build();
    }

    // ========== 사용자별 최근 검색어 ==========
    public List<String> getRecentSearchesByUser(Long userId, String clientIp) {
        return searchHistory.stream()
                .filter(event -> clientIp.equals(event.getUserIp()))
                .sorted((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()))
                .map(SearchEvent::getQuery)
                .distinct()
                .limit(10)
                .collect(Collectors.toList());
    }

    // ========== 트렌딩 검색어 조회 ==========
    public List<TrendingSearch> getTrendingSearches() {
        return esRepository.getTrendingSearches();
    }

    // ========== 개인화 추천 ==========
    public List<String> getPersonalizedSuggestions(HttpServletRequest request, Long userId) {
        String clientIp = getClientIpAddress(request);

        // 사용자 검색 히스토리 기반 추천
        List<String> recentSearches = getRecentSearchesByUser(userId, clientIp);
        return generatePersonalizedSuggestions(recentSearches);
    }

    // ========== Private 메서드들 ==========
    private List<String> getTopQueries(List<SearchEvent> events) {
        return events.stream()
                .collect(Collectors.groupingBy(SearchEvent::getQuery, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(10)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private List<String> getTopCorrectedQueries(List<SearchEvent> events) {
        return events.stream()
                .filter(event -> !event.getQuery().equals(event.getParsedParams().getCorrectedQuery()))
                .map(event -> event.getQuery() + " → " + event.getParsedParams().getCorrectedQuery())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(10)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private Map<String, Integer> getSearchesByHour(List<SearchEvent> events) {
        return events.stream()
                .collect(Collectors.groupingBy(
                        event -> String.valueOf(event.getTimestamp().atZone(ZoneId.systemDefault()).getHour()),
                        Collectors.summingInt(e -> 1)
                ));
    }

    private Map<String, Integer> getSearchesByCategory(List<SearchEvent> events) {
        return events.stream()
                .filter(event -> event.getParsedParams().getCategory() != null)
                .collect(Collectors.groupingBy(
                        event -> event.getParsedParams().getCategory(),
                        Collectors.summingInt(e -> 1)
                ));
    }

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

    private List<String> generatePersonalizedSuggestions(List<String> recentSearches) {
        return SearchSuggestionKeywords.generatePersonalizedSuggestions(recentSearches);
    }
}