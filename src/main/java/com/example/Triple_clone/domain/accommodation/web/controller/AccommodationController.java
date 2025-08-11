package com.example.Triple_clone.domain.accommodation.web.controller;

import com.example.Triple_clone.domain.accommodation.application.AccommodationQueryService;
import com.example.Triple_clone.domain.accommodation.domain.AccommodationDocument;
import com.example.Triple_clone.domain.accommodation.domain.SortOption;
import com.example.Triple_clone.domain.accommodation.web.dto.AutocompleteResult;
import com.example.Triple_clone.domain.accommodation.web.dto.SpellCheckResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/accommodations")
@RequiredArgsConstructor
public class AccommodationController {

    private final AccommodationQueryService accommodationQueryService;

    @GetMapping("/autocomplete")
    public ResponseEntity<List<AutocompleteResult>> smartAutocomplete(
            @RequestParam String q,
            @RequestParam(defaultValue = "10") int limit
    ) {
        List<AutocompleteResult> results = accommodationQueryService.getSmartAutocomplete(q, limit);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/typo-correction")
    public ResponseEntity<SpellCheckResponse> checkSpelling(
            @RequestParam String query
    ) {
        SpellCheckResponse response = accommodationQueryService.checkSpelling(query);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<AccommodationDocument>> searchAccommodations(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "ID_ASC") SortOption sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        List<AccommodationDocument> results = accommodationQueryService.searchAccommodations(q, sort, page, size);
        return ResponseEntity.ok(results);
    }
}