package com.example.Triple_clone.domain.accommodation.application;

import com.example.Triple_clone.domain.accommodation.infra.ESAccommodationRepositoryImpl;
import com.example.Triple_clone.domain.accommodation.web.dto.AutocompleteResult;
import com.example.Triple_clone.domain.accommodation.web.dto.SearchSuggestionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class NaturalLanguageSearchService {
    private final ESAccommodationRepositoryImpl esRepository;

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
}