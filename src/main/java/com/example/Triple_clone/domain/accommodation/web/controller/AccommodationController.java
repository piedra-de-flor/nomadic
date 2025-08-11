package com.example.Triple_clone.domain.accommodation.web.controller;

import com.example.Triple_clone.domain.accommodation.application.AccommodationQueryService;
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
}