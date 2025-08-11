package com.example.Triple_clone.domain.accommodation.application;

import com.example.Triple_clone.common.logging.logMessage.RecommendLogMessage;
import com.example.Triple_clone.domain.accommodation.domain.Accommodation;
import com.example.Triple_clone.domain.accommodation.infra.AccommodationRepository;
import com.example.Triple_clone.domain.accommodation.web.dto.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccommodationQueryService {
    private final AccommodationRepository repository;
    private final AccommodationSearchService searchService;

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
            List<AutocompleteResult> results = searchService.getAutocomplete(query, limit);
            log.debug("자동완성 완료: q='{}', 결과={}건", query, results.size());
            return results;
        } catch (Exception e) {
            log.error("자동완성 오류: q='{}', error='{}'", query, e.getMessage(), e);
            return List.of();
        }
    }

    // ========== 오타 교정 관련 메서드 ==========
    public SpellCheckResponse checkSpelling(String query) {
        try {
            log.debug("오타 교정 요청: query='{}'", query);
            SpellCheckResponse response = searchService.checkSpelling(query);
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
}