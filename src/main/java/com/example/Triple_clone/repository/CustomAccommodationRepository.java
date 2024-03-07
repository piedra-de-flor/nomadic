package com.example.Triple_clone.repository;

import com.example.Triple_clone.domain.entity.Accommodation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomAccommodationRepository {
    Page<Accommodation> findAllByConditions(String local,
                                            String name,
                                            String startLentPrice,
                                            String endLentPrice,
                                            String category,
                                            String score,
                                            String lentStatus,
                                            String enterTime,
                                            String discountRate,
                                            String startTotalPrice,
                                            String endTotalPrice,
                                            Pageable pageable);
}
