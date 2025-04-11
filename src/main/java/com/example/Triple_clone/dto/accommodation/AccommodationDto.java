package com.example.Triple_clone.dto.accommodation;

import com.example.Triple_clone.domain.entity.Accommodation;
import com.example.Triple_clone.domain.entity.AccommodationDocument;

public record AccommodationDto(
        String local,
        String name,
        double score,
        String category,
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
                document.getScore(),
                document.getCategory(),
                document.getLentDiscountRate(),
                document.getLentTime(),
                document.getLentOriginPrice(),
                document.getLentPrice(),
                document.getLentStatus(),
                String.valueOf(document.getEnterTime()),
                document.getLodgmentDiscountRate(),
                document.getLodgmentOriginPrice(),
                document.getLodgmentPrice(),
                document.getLodgmentStatus()
        );
    }
}
