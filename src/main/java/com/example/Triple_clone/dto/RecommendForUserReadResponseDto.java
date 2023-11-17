package com.example.Triple_clone.dto;

import com.example.Triple_clone.entity.Place;
import com.example.Triple_clone.vo.entity.Photo;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RecommendForUserReadResponseDto {
    private final Long id;
    private final String title;
    private final String notionUrl;
    private final String subTitle;
    private final String location;
    private final Photo mainPhoto;
    private final String date;
    private final Boolean like;

    public RecommendForUserReadResponseDto(Place place, boolean like) {
        this.id = place.getId();
        this.title = place.getTitle();
        this.notionUrl = place.getNotionUrl();
        this.subTitle = place.getSubTitle();
        this.location = place.getLocation();
        this.mainPhoto = place.getMainPhoto();
        this.date = place.getDate();
        this.like = like;
    }
}
