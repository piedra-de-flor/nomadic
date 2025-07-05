package com.example.Triple_clone.domain.accommodation.infra;

import com.example.Triple_clone.domain.accommodation.domain.AccommodationDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ESAccommodationRepository {
    Page<AccommodationDocument> searchByConditionsFromES(
            String local, String name, String category, String discountRate,
            String startLentPrice, String endLentPrice, String score,
            String lentStatus, String startLodgmentPrice, String endLodgmentPrice,
            String enterTime,String lodgmentStatus, Pageable pageable);
}
