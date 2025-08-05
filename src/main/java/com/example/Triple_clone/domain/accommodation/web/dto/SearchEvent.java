package com.example.Triple_clone.domain.accommodation.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchEvent {
    private String query;
    private SearchParams parsedParams;
    private Long resultCount;
    private Long searchTime;
    private String userIp;
    private String userAgent;
    private Instant timestamp;
}
