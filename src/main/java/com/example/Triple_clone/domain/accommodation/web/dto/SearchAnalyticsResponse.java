package com.example.Triple_clone.domain.accommodation.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchAnalyticsResponse {
    private Long totalSearches;
    private Double averageSearchTime;
    private List<String> topQueries;
    private List<String> topCorrectedQueries;
    private Map<String, Integer> searchesByHour;
    private Map<String, Integer> searchesByCategory;
}
