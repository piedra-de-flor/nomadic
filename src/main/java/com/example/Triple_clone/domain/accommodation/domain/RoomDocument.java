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
    private Integer id;
    private String name;
    private Integer dayusePrice;
    private String dayuseTime;
    private String stayCheckinTime;
    private String stayCheckoutTime;
    private Integer stayPrice;
    private Integer capacity;
    private Integer maxCapacity;
}
