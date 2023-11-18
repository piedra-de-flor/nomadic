package com.example.Triple_clone.dto.recommend.user;

import com.example.Triple_clone.entity.Place;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RecommendForUserReadResponseDto {
    private final Long id;
    private final String title;
    private final String notionUrl;
    private final String subTitle;
    private final String location;
    private final String mainImage;
    private final LocalDateTime date;
    private final Boolean like;

    public RecommendForUserReadResponseDto(Place place, boolean like) {
        this.id = place.getId();
        this.title = place.getTitle();
        this.notionUrl = place.getNotionUrl();
        this.subTitle = place.getSubTitle();
        this.location = place.getLocation();
        this.mainImage = place.getMainImage();
        this.date = place.getDate();
        this.like = like;
    }
}
