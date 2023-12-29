package com.example.Triple_clone.repository;

import com.example.Triple_clone.domain.entity.Place;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    Page<Place> findAllByOrderByTitleDesc(Pageable pageable);

    Page<Place> findAllByOrderByDateDesc(Pageable pageable);
}
