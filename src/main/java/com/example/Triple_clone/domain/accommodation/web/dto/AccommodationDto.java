package com.example.Triple_clone.domain.accommodation.web.dto;

import com.example.Triple_clone.domain.accommodation.domain.Accommodation;
import com.example.Triple_clone.domain.accommodation.domain.AccommodationDocument;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AccommodationDto {
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
    private Integer minStayPrice;
    private RoomDto previewRoom;

    public static AccommodationDto fromDocument(AccommodationDocument doc) {
        return AccommodationDto.builder()
                .id(doc.getId())
                .image(doc.getImage())
                .name(doc.getName())
                .category(doc.getCategory())
                .grade(doc.getGrade())
                .rating(doc.getRating())
                .reviewCount(doc.getReviewCount())
                .region(doc.getRegion())
                .address(doc.getAddress())
                .landmarkDistance(doc.getLandmarkDistance())
                .minStayPrice(doc.getMinStayPrice())
                .previewRoom(RoomDto.fromDocument(doc.getPreviewRoom()))
                .build();
    }

    public static AccommodationDto fromEntity(Accommodation entity) {
        return AccommodationDto.builder()
                .id(entity.getId())
                .image(entity.getImage())
                .name(entity.getName())
                .category(entity.getCategory())
                .grade(entity.getGrade())
                .rating(entity.getRating())
                .reviewCount(entity.getReviewCount())
                .region(entity.getRegion())
                .address(entity.getAddress())
                .landmarkDistance(entity.getLandmarkDistance())
                .build();
    }
}
