package com.example.Triple_clone.domain.accommodation.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoomDocument {
    private long id;
    private String name;
    private Integer dayusePrice;
    private Integer dayuseSalePrice;
    private Boolean hasDayuseDiscount;
    private Boolean dayuseSoldout;
    private String dayuseTime;
    private Integer stayPrice;
    private Integer staySalePrice;
    private Boolean hasStayDiscount;
    private Boolean staySoldout;
    private String stayCheckinTime;
    private String stayCheckoutTime;
    private Integer capacity;
    private Integer maxCapacity;
}
