package com.example.Triple_clone.domain.accommodation.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchSuggestionResponse {
    private List<String> similarQueries = new ArrayList<>();
    private List<String> popularSearches = new ArrayList<>();
    private List<String> contextSuggestions = new ArrayList<>();
    private List<String> corrections = new ArrayList<>();
}