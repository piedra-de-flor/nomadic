package com.example.Triple_clone.domain.accommodation.web.dto;

import com.example.Triple_clone.domain.accommodation.domain.AccommodationDocument;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponse {
    private Page<AccommodationDocument> results;
    private SearchParams searchParams;
    private SearchSuggestionResponse suggestions;
    private Long searchTime;
    private Long totalResults;
    private String error;
}
