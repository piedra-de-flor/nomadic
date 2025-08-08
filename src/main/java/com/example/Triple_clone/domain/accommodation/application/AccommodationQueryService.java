package com.example.Triple_clone.domain.accommodation.application;

import com.example.Triple_clone.common.logging.logMessage.RecommendLogMessage;
import com.example.Triple_clone.domain.accommodation.domain.Accommodation;
import com.example.Triple_clone.domain.accommodation.infra.AccommodationRepository;
import com.example.Triple_clone.domain.accommodation.web.dto.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccommodationQueryService {
    private final AccommodationRepository repository;

    private final NaturalLanguageSearchService naturalLanguageSearchService;
    private final SearchAnalyticsService searchAnalyticsService;
    private final SpellCheckService spellCheckService;

    public Accommodation findById(long accommodationId) {
        Accommodation accommodation = repository.findById(accommodationId)
                .orElseThrow(() -> {
                    log.warn(RecommendLogMessage.RECOMMEND_SEARCH_FAILED.format("MySQL 숙소 조회 실패", accommodationId));
                    return new EntityNotFoundException("no accommodation entity");
                });

        return accommodation;
    }

    // ========== 자동완성 관련 메서드 ==========
    public List<AutocompleteResult> getSmartAutocomplete(String query, int limit) {
        try {
            log.debug("자동완성 요청: q='{}', limit={}", query, limit);
            List<AutocompleteResult> results = naturalLanguageSearchService.getSmartAutocomplete(query, limit);
            log.debug("자동완성 완료: q='{}', 결과={}건", query, results.size());
            return results;
        } catch (Exception e) {
            log.error("자동완성 오류: q='{}', error='{}'", query, e.getMessage(), e);
            return List.of();
        }
    }

    // ========== 검색 제안 관련 메서드 ==========
    public SearchSuggestionResponse getSearchSuggestions(String query) {
        try {
            log.debug("검색 제안 요청: query='{}'", query);
            SearchSuggestionResponse suggestions = naturalLanguageSearchService.getSearchSuggestions(query);
            log.debug("검색 제안 완료: query='{}'", query);
            return suggestions;
        } catch (Exception e) {
            log.error("검색 제안 오류: query='{}', error='{}'", query, e.getMessage(), e);
            return new SearchSuggestionResponse();
        }
    }

    // ========== 트렌딩 검색어 관련 메서드 ==========
    public List<TrendingSearch> getTrendingSearches() {
        try {
            log.debug("트렌딩 검색어 요청");
            List<TrendingSearch> trending = searchAnalyticsService.getTrendingSearches();
            log.debug("트렌딩 검색어 완료: 결과={}건", trending.size());
            return trending;
        } catch (Exception e) {
            log.error("트렌딩 검색어 오류: error='{}'", e.getMessage(), e);
            return List.of();
        }
    }

    // ========== 검색 분석 관련 메서드 (관리자용) ==========
    public SearchAnalyticsResponse getSearchAnalytics(int days) {
        try {
            log.info("검색 분석 요청: days={}", days);
            SearchAnalyticsResponse analytics = searchAnalyticsService.getSearchAnalytics(days);
            log.info("검색 분석 완료: days={}, 총 검색={}건", days, analytics.getTotalSearches());
            return analytics;
        } catch (Exception e) {
            log.error("검색 분석 오류: days={}, error='{}'", days, e.getMessage(), e);
            // 관리자용 API이므로 에러 정보를 더 상세히 제공
            return SearchAnalyticsResponse.builder()
                    .totalSearches(0L)
                    .averageSearchTime(0.0)
                    .build();
        }
    }

    // ========== 오타 교정 관련 메서드 ==========
    public SpellCheckResponse checkSpelling(String query) {
        try {
            log.debug("오타 교정 요청: query='{}'", query);
            SpellCheckResponse response = spellCheckService.checkSpelling(query);
            log.debug("오타 교정 완료: query='{}', 교정여부={}", query, response.getHasCorrection());
            return response;
        } catch (Exception e) {
            log.error("오타 교정 오류: query='{}', error='{}'", query, e.getMessage(), e);
            return SpellCheckResponse.builder()
                    .originalQuery(query)
                    .correctedQuery(query)
                    .hasCorrection(false)
                    .build();
        }
    }

    // ========== 개인화 추천 관련 메서드 ==========
    public List<String> getPersonalizedSuggestions(HttpServletRequest request, Long userId) {
        try {
            log.debug("개인화 추천 요청: userId={}", userId);
            List<String> suggestions = searchAnalyticsService.getPersonalizedSuggestions(request, userId);
            log.debug("개인화 추천 완료: userId={}, 결과={}건", userId, suggestions.size());
            return suggestions;
        } catch (Exception e) {
            log.error("개인화 추천 오류: userId={}, error='{}'", userId, e.getMessage(), e);
            return List.of();
        }
    }
}