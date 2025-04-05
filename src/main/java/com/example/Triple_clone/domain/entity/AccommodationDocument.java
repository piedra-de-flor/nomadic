package com.example.Triple_clone.domain.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
