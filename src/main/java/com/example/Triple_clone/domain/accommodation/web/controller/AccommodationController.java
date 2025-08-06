package com.example.Triple_clone.domain.accommodation.web.controller;

import com.example.Triple_clone.domain.accommodation.application.AccommodationQueryService;
import com.example.Triple_clone.domain.accommodation.web.dto.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/accommodations")
@RequiredArgsConstructor
public class AccommodationController {

    private final AccommodationQueryService accommodationQueryService;

    @GetMapping("/natural-search")
    public ResponseEntity<SearchResponse> naturalLanguageSearch(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request
    ) {
        Pageable pageable = PageRequest.of(page, size);
        SearchResponse response = accommodationQueryService.naturalLanguageSearch(query, pageable, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/smart-autocomplete")
    public ResponseEntity<List<AutocompleteResult>> smartAutocomplete(
            @RequestParam String q,
            @RequestParam(defaultValue = "10") int limit
    ) {
        List<AutocompleteResult> results = accommodationQueryService.getSmartAutocomplete(q, limit);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/search-suggestions")
    public ResponseEntity<SearchSuggestionResponse> getSearchSuggestions(
            @RequestParam String query
    ) {
        SearchSuggestionResponse suggestions = accommodationQueryService.getSearchSuggestions(query);
        return ResponseEntity.ok(suggestions);
    }

    @GetMapping("/trending")
    public ResponseEntity<List<TrendingSearch>> getTrendingSearches() {
        List<TrendingSearch> trending = accommodationQueryService.getTrendingSearches();
        return ResponseEntity.ok(trending);
    }

    @GetMapping("/admin/search-analytics")
    public ResponseEntity<SearchAnalyticsResponse> getSearchAnalytics(
            @RequestParam(defaultValue = "7") int days
    ) {
        SearchAnalyticsResponse analytics = accommodationQueryService.getSearchAnalytics(days);
        return ResponseEntity.ok(analytics);
    }

    @GetMapping("/spell-check")
    public ResponseEntity<SpellCheckResponse> checkSpelling(
            @RequestParam String query
    ) {
        SpellCheckResponse response = accommodationQueryService.checkSpelling(query);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/personalized-suggestions")
    public ResponseEntity<List<String>> getPersonalizedSuggestions(
            HttpServletRequest request,
            @RequestParam(required = false) Long userId
    ) {
        List<String> suggestions = accommodationQueryService.getPersonalizedSuggestions(request, userId);
        return ResponseEntity.ok(suggestions);
    }
}