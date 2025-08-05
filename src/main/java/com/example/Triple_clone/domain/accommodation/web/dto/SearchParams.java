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
public class SearchParams {
    private String searchKeyword;
    private String category;
    private Float ratingMin;
    private Float ratingMax;
    private String region;
    private Integer roomPriceMin;
    private Integer roomPriceMax;
    private Integer roomCapacityMin;
    private Integer roomCapacityMax;
    private Boolean hasDiscount;
    private Boolean lateCheckout;
    private Boolean earlyCheckin;

    private List<String> locationContext = new ArrayList<>();
    private List<String> facilities = new ArrayList<>();
    private List<String> roomTypes = new ArrayList<>();
    private List<String> suggestions = new ArrayList<>();
    private String originalQuery;
    private String correctedQuery;

    public void addLocationContext(String location) {
        if (locationContext == null) {
            locationContext = new ArrayList<>();
        }
        if (location != null && !location.trim().isEmpty()) {
            locationContext.add(location);
        }
    }

    public void addFacility(String facility) {
        if (facilities == null) {
            facilities = new ArrayList<>();
        }
        if (facility != null && !facility.trim().isEmpty()) {
            facilities.add(facility);
        }
    }

    public void addRoomType(String roomType) {
        if (roomTypes == null) {
            roomTypes = new ArrayList<>();
        }
        if (roomType != null && !roomType.trim().isEmpty()) {
            roomTypes.add(roomType);
        }
    }
}