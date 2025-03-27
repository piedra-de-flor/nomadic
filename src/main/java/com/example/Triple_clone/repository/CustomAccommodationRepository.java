package com.example.Triple_clone.repository;

import com.example.Triple_clone.domain.entity.Accommodation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomAccommodationRepository {
    Page<Accommodation> searchByConditionsFromDB(String local,
                                                   String name,
                                                   String discountRate,
                                                   String startLentPrice,
                                                   String endLentPrice,
                                                   String category,
                                                   String score,
                                                   String lentStatus,
                                                   String enterTime,
                                                   String startLodgmentPrice,
                                                   String endLodgmentPrice,
                                                   String lodgementStatus,
                                                   Pageable pageable);
}
