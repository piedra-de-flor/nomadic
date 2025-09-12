package com.example.Triple_clone.domain.accommodation.web.dto;

import com.example.Triple_clone.domain.accommodation.domain.Accommodation;
import com.example.Triple_clone.domain.accommodation.domain.Room;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccommodationDto {
    private Long id;
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
    private List<RoomDto> rooms;

    public static AccommodationDto from(Accommodation accommodation) {
        return AccommodationDto.builder()
                .id(accommodation.getId())
                .image(accommodation.getImage())
                .name(accommodation.getName())
                .category(accommodation.getCategory())
                .grade(accommodation.getGrade())
                .rating(accommodation.getRating())
                .reviewCount(accommodation.getReviewCount())
                .region(accommodation.getRegion())
                .address(accommodation.getAddress())
                .landmarkDistance(accommodation.getLandmarkDistance())
                .intro(accommodation.getIntro())
                .amenities(accommodation.getAmenities())
                .info(accommodation.getInfo())
                .rooms(accommodation.getRooms().stream()
                        .map(RoomDto::from)
                        .collect(Collectors.toList()))
                .build();
    }

    public Accommodation toEntity() {
        return Accommodation.builder()
                .image(this.image)
                .name(this.name)
                .category(this.category)
                .grade(this.grade)
                .rating(this.rating)
                .reviewCount(this.reviewCount)
                .region(this.region)
                .address(this.address)
                .landmarkDistance(this.landmarkDistance)
                .intro(this.intro)
                .amenities(this.amenities)
                .info(this.info)
                .build();
    }
}