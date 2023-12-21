package com.example.Triple_clone.repository;

import com.example.Triple_clone.entity.Place;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    Page<Place> findAllByOrderByTitleDesc(Pageable pageable);

    Page<Place> findAllByOrderByDateDesc(Pageable pageable);
}
