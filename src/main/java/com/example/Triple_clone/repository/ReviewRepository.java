package com.example.Triple_clone.repository;

import com.example.Triple_clone.domain.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
