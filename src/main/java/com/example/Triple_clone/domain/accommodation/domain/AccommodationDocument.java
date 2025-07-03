package com.example.Triple_clone.domain.accommodation.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccommodationDocument {
    private Long id;
    private String category;
    private String local;
    private String name;
    private Double score;
    private String enterTime;
    private Long lentDiscountRate;
    private Long lentOriginPrice;
    private Long lentPrice;
    private Boolean lentStatus;
    private Integer lentTime;
    private Long lodgmentDiscountRate;
    private Long lodgmentOriginPrice;
    private Long lodgmentPrice;
    private Boolean lodgmentStatus;
}
