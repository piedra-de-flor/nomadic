package com.example.Triple_clone.domain.accommodation.application;

import com.example.Triple_clone.domain.accommodation.domain.AccommodationDocument;
import com.example.Triple_clone.domain.accommodation.domain.SortOption;
import com.example.Triple_clone.domain.accommodation.infra.ESAccommodationRepository;
import com.example.Triple_clone.domain.accommodation.web.dto.AutocompleteResult;
import com.example.Triple_clone.domain.accommodation.web.dto.SpellCheckResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccommodationSearchService {

    private final ESAccommodationRepository esRepository;

    public List<AutocompleteResult> getAutocomplete(String query, int limit) {
        try {
            log.debug("자동완성 요청: q='{}', limit={}", query, limit);

            if (query == null || query.trim().length() < 1) {
                return List.of();
            }

            List<AutocompleteResult> results = esRepository.searchAccommodationNames(query.trim(), limit);
            log.debug("자동완성 완료: q='{}', 결과={}건", query, results.size());
            return results;

        } catch (Exception e) {
            log.error("자동완성 오류: q='{}', error='{}'", query, e.getMessage(), e);
            return List.of();
        }
    }

    public List<AccommodationDocument> searchWithSort(String query, SortOption sortOption, int page, int size) {
        try {
            log.debug("정렬 검색 요청: query='{}', sort={}, page={}, size={}", query, sortOption, page, size);

            List<AccommodationDocument> results = esRepository.searchAccommodationsWithSort(query, sortOption, page, size);
            log.debug("정렬 검색 완료: query='{}', 결과={}건", query, results.size());
            return results;

        } catch (Exception e) {
            log.error("정렬 검색 오류: query='{}', sort={}, error='{}'", query, sortOption, e.getMessage(), e);
            return List.of();
        }
    }

    public SpellCheckResponse checkSpelling(String query) {
        try {
            log.debug("오타 교정 요청: query='{}'", query);

            if (query == null || query.trim().isEmpty()) {
                return SpellCheckResponse.builder()
                        .originalQuery(query)
                        .correctedQuery(query)
                        .hasCorrection(false)
                        .build();
            }

            String trimmedQuery = query.trim();
            String bestMatch = esRepository.findBestMatch(trimmedQuery);

            boolean hasCorrection = !trimmedQuery.equals(bestMatch);

            SpellCheckResponse response = SpellCheckResponse.builder()
                    .originalQuery(trimmedQuery)
                    .correctedQuery(bestMatch)
                    .hasCorrection(hasCorrection)
                    .build();

            if (hasCorrection) {
                List<String> suggestions = esRepository.getSimilarAccommodationNames(bestMatch, 3);
                response.setSuggestions(suggestions);
                log.info("오타 교정됨: '{}' → '{}'", trimmedQuery, bestMatch);
            }

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