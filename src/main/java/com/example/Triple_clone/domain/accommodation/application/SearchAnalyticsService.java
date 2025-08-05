package com.example.Triple_clone.domain.accommodation.application;

import com.example.Triple_clone.domain.accommodation.web.dto.SearchAnalyticsResponse;
import com.example.Triple_clone.domain.accommodation.web.dto.SearchEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SearchAnalyticsService {
    private final List<SearchEvent> searchHistory = new CopyOnWriteArrayList<>();

    public void recordSearch(SearchEvent event) {
        searchHistory.add(event);

        if (searchHistory.size() > 10000) {
            searchHistory.subList(0, searchHistory.size() - 10000).clear();
        }

        log.debug("검색 이벤트 기록: {}", event);
    }

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

    public List<String> getRecentSearchesByUser(Long userId, String clientIp) {
        return searchHistory.stream()
                .filter(event -> clientIp.equals(event.getUserIp()))
                .sorted((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()))
                .map(SearchEvent::getQuery)
                .distinct()
                .limit(10)
                .collect(Collectors.toList());
    }

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
}