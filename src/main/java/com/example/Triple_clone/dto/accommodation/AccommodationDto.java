package com.example.Triple_clone.dto.accommodation;

import com.example.Triple_clone.domain.entity.Accommodation;

public record AccommodationDto(
        String local,
        String name,
        double score,
        String category,
        int lentTime,
        long lentPrice,
        boolean lentStatus,
        String enterTime,
        long discountRate,
        long originPrice,
        long totalPrice
) {
    public Accommodation toEntity() {
        return Accommodation.builder()
                .local(local)
                .name(name)
                .score(score)
                .category(category)
                .lentTime(lentTime)
                .lentPrice(lentPrice)
                .lentStatus(lentStatus)
                .enterTime(enterTime)
                .lodgmentDiscountRate(discountRate)
                .lodgmentOriginPrice(originPrice)
                .lodgmentPrice(totalPrice)
                .build();
    }

    public AccommodationDto(Accommodation accommodation) {
        this(
                accommodation.getLocal(),
                accommodation.getName(),
                accommodation.getScore(),
                accommodation.getCategory(),
                accommodation.getLentTime(),
                accommodation.getLentPrice(),
                accommodation.isLentStatus(),
                String.valueOf(accommodation.getEnterTime()),
                accommodation.getLodgmentDiscountRate(),
                accommodation.getLodgmentOriginPrice(),
                accommodation.getLodgmentPrice()
        );
    }
}
