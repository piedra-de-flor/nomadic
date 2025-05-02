package com.example.Triple_clone.repository;

import com.example.Triple_clone.domain.entity.Accommodation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {
    List<Accommodation> readAllByLocal(String local);
}
