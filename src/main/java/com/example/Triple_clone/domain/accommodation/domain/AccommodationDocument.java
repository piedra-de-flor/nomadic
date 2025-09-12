package com.example.Triple_clone.domain.accommodation.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccommodationDocument {
    private long id;
    private String image;
    private String name;
    private String category;
    private String grade;
    private Float rating;
    private Integer reviewCount;
    private String region;
    private String address;
    private String landmarkDistance;
    private String intro;
    private String amenities;
    private String info;
    private Integer minStayPrice;
    private RoomDocument previewRoom;
    private List<RoomDocument> rooms;
}
