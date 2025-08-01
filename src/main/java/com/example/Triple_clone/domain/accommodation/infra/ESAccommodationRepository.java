package com.example.Triple_clone.domain.accommodation.infra;

import com.example.Triple_clone.domain.accommodation.domain.AccommodationDocument;
import com.example.Triple_clone.domain.accommodation.domain.SortOption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ESAccommodationRepository {
    Page<AccommodationDocument> searchByConditionsFromES(
            String searchKeyword,
            String category,
            Float ratingMin, Float ratingMax,
            String region,
            Integer dayusePriceMin, Integer dayusePriceMax,
            Boolean dayuseAvailable,
            Boolean hasDayuseDiscount,
            Integer stayPriceMin, Integer stayPriceMax,
            Boolean stayAvailable,
            Boolean hasStayDiscount,
            Integer roomPriceMin, Integer roomPriceMax,
            Integer roomCapacityMin, Integer roomCapacityMax,
            String roomCheckoutTime,
            SortOption sortOption,
            Pageable pageable
    );

    List<String> autocompleteName(String prefix);
}

