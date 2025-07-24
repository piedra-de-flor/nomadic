package com.example.Triple_clone.domain.accommodation.web.dto;

import com.example.Triple_clone.domain.accommodation.domain.Accommodation;
import com.example.Triple_clone.domain.accommodation.domain.AccommodationDocument;

public record AccommodationDto(
        long id,
        String image,
        String name,
        String category,
        String grade,
        Float rating,
        Integer reviewCount,
        String region,
        String address,
        String landmarkDistance,
        Boolean hasDayuseDiscount,
        Integer dayusePrice,
        Integer dayuseSalePrice,
        Boolean dayuseSoldout,
        String dayuseTime,
        Boolean hasStayDiscount,
        Integer stayPrice,
        Integer staySalePrice,
        Boolean staySoldout,
        String stayCheckinTime,
        String intro,
        String amenities,
        String info
) {
    public AccommodationDto(Accommodation accommodation) {
        this(
                accommodation.getId(),
                accommodation.getImage(),
                accommodation.getName(),
                accommodation.getCategory(),
                accommodation.getGrade(),
                accommodation.getRating(),
                accommodation.getReviewCount(),
                accommodation.getRegion(),
                accommodation.getAddress(),
                accommodation.getLandmarkDistance(),
                accommodation.getHasDayuseDiscount(),
                accommodation.getDayusePrice(),
                accommodation.getDayuseSalePrice(),
                accommodation.getDayuseSoldout(),
                accommodation.getDayuseTime(),
                accommodation.getHasStayDiscount(),
                accommodation.getStayPrice(),
                accommodation.getStaySalePrice(),
                accommodation.getStaySoldout(),
                accommodation.getStayCheckinTime(),
                accommodation.getIntro(),
                accommodation.getAmenities(),
                accommodation.getInfo()
        );
    }

    public AccommodationDto(AccommodationDocument doc) {
        this(
                Long.parseLong(String.valueOf(doc.getId())),
                doc.getImage(),
                doc.getName(),
                doc.getCategory(),
                doc.getGrade(),
                doc.getRating(),
                doc.getReviewCount(),
                doc.getRegion(),
                doc.getAddress(),
                doc.getLandmarkDistance(),
                doc.getHasDayuseDiscount(),
                doc.getDayusePrice(),
                doc.getDayuseSalePrice(),
                doc.getDayuseSoldout(),
                doc.getDayuseTime(),
                doc.getHasStayDiscount(),
                doc.getStayPrice(),
                doc.getStaySalePrice(),
                doc.getStaySoldout(),
                doc.getStayCheckinTime(),
                doc.getIntro(),
                doc.getAmenities(),
                doc.getInfo()
        );
    }

    public Accommodation toEntity() {
        return Accommodation.builder()
                .image(image)
                .name(name)
                .category(category)
                .grade(grade)
                .rating(rating)
                .reviewCount(reviewCount)
                .region(region)
                .address(address)
                .landmarkDistance(landmarkDistance)
                .hasDayuseDiscount(hasDayuseDiscount)
                .dayusePrice(dayusePrice)
                .dayuseSalePrice(dayuseSalePrice)
                .dayuseSoldout(dayuseSoldout)
                .dayuseTime(dayuseTime)
                .hasStayDiscount(hasStayDiscount)
                .stayPrice(stayPrice)
                .staySalePrice(staySalePrice)
                .staySoldout(staySoldout)
                .stayCheckinTime(stayCheckinTime)
                .intro(intro)
                .amenities(amenities)
                .info(info)
                .build();
    }
}