package com.example.Triple_clone.domain.accommodation.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AutocompleteResult {
    private String text;
    private String type;
    private Double score;
    private Long accommodationId;
    private Long roomId;
}