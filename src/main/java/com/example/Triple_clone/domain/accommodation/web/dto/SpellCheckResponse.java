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
public class SpellCheckResponse {
    private String originalQuery;
    private String correctedQuery;
    private Boolean hasCorrection;
    private List<String> suggestions = new ArrayList<>();
}
