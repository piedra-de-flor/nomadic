package com.example.Triple_clone.repository;

import com.example.Triple_clone.domain.entity.Accommodation;

import java.util.List;

public interface CustomAccommodationRepository {
    List<Accommodation> findAllByConditions(String local,
                                            String name,
                                            String startLentPrice,
                                            String endLentPrice,
                                            String category,
                                            String score,
                                            String lentStatus,
                                            String enterTime,
                                            String discountRate,
                                            String startTotalPrice,
                                            String endTotalPrice);
}
