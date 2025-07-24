package com.example.Triple_clone.domain.accommodation.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccommodationDocument {
    private Integer id;
    private String image;
    private String name;
    private String category;
    private String grade;
    private Float rating;
    private Integer reviewCount;
    private String region;
    private String address;
    private String landmarkDistance;
    private Boolean hasDayuseDiscount;
    private Integer dayusePrice;
    private Integer dayuseSalePrice;
    private Boolean dayuseSoldout;
    private String dayuseTime;
    private Boolean hasStayDiscount;
    private Integer stayPrice;
    private Integer staySalePrice;
    private Boolean staySoldout;
    private String stayCheckinTime;
    private String intro;
    private String amenities;
    private String info;
    private List<RoomDocument> rooms;
}
