package com.example.Triple_clone.dto.recommend.user;

import com.example.Triple_clone.entity.Place;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public record RecommendForUserReadResponseDto (
    long id,
    String title,
    String notionUrl,
    String subTitle,
    String location,
    String mainImage,
    LocalDateTime date,
    boolean like) {

    public RecommendForUserReadResponseDto(Place place, boolean like) {
        this(place.getId(), place.getTitle(), place.getNotionUrl(), place.getSubTitle(), place.getLocation(), place.getMainImage(), place.getDate(), like);
    }
}
