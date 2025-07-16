package com.example.Triple_clone.domain.accommodation.web.dto;

import com.example.Triple_clone.domain.accommodation.domain.Accommodation;
import com.example.Triple_clone.domain.accommodation.domain.AccommodationDocument;

public record AccommodationDto(
        String local,
        String name,
        double score,
        String category,
        String imageUrl,
        String description,
        String detailDescription,
        String services,
        long lentDiscountRate,
        int lentTime,
        long lentOriginPrice,
        long lentPrice,
        boolean lentStatus,
        String enterTime,
        long lodgmentDiscountRate,
        long lodgmentOriginPrice,
        long lodgmentPrice,
        boolean lodgmentStatus
) {
    public Accommodation toEntity() {
        return Accommodation.builder()
                .local(local)
                .name(name)
                .score(score)
                .category(category)
                .imageUrl(imageUrl)
                .description(description)
                .detailDescription(detailDescription)
                .services(services)
                .lentTime(lentTime)
                .lentPrice(lentPrice)
                .lentOriginPrice(lentOriginPrice)
                .lentStatus(lentStatus)
                .lentDiscountRate(lentDiscountRate)
                .lodgmentPrice(lodgmentPrice)
                .lodgmentStatus(lodgmentStatus)
                .lodgmentOriginPrice(lodgmentOriginPrice)
                .lodgmentDiscountRate(lodgmentDiscountRate)
                .enterTime(enterTime)
                .build();
    }

    public AccommodationDto(Accommodation accommodation) {
        this(
                accommodation.getLocal(),
                accommodation.getName(),
                accommodation.getScore(),
                accommodation.getCategory(),
                accommodation.getImageUrl(),
                accommodation.getDescription(),
                accommodation.getDetailDescription(),
                accommodation.getServices(),
                accommodation.getLentDiscountRate(),
                accommodation.getLentTime(),
                accommodation.getLentOriginPrice(),
                accommodation.getLentPrice(),
                accommodation.isLentStatus(),
                String.valueOf(accommodation.getEnterTime()),
                accommodation.getLodgmentDiscountRate(),
                accommodation.getLodgmentOriginPrice(),
                accommodation.getLodgmentPrice(),
                accommodation.isLodgmentStatus()
        );
    }

    public AccommodationDto(AccommodationDocument document) {
        this(
                document.getLocal(),
                document.getName(),
                document.getScore() != null ? document.getScore() : 0.0,
                document.getCategory(),
                document.getImageUrl(),
                document.getDescription(),
                document.getDetailDescription(),
                document.getServices(),
                document.getLentDiscountRate() != null ? document.getLentDiscountRate() : 0L,
                document.getLentTime() != null ? document.getLentTime() : 0,
                document.getLentOriginPrice() != null ? document.getLentOriginPrice() : 0L,
                document.getLentPrice() != null ? document.getLentPrice() : 0L,
                document.getLentStatus() != null ? document.getLentStatus() : false,
                document.getEnterTime() != null ? document.getEnterTime() : "",
                document.getLodgmentDiscountRate() != null ? document.getLodgmentDiscountRate() : 0L,
                document.getLodgmentOriginPrice() != null ? document.getLodgmentOriginPrice() : 0L,
                document.getLodgmentPrice() != null ? document.getLodgmentPrice() : 0L,
                document.getLodgmentStatus() != null ? document.getLodgmentStatus() : false
        );
    }
}
