package com.example.Triple_clone.service.review;

import com.example.Triple_clone.domain.entity.Review;
import com.example.Triple_clone.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class ReviewService {
    private final ReviewRepository repository;

    public void save(Review review) {
        repository.save(review);
    }

    public void delete(Review review) {
        repository.delete(review);
    }

    public void update(Review review, String content) {
        review.update(content);
    }

    public Review findById(Long reviewId) {
        return repository.findById(reviewId)
                .orElseThrow(NoSuchElementException::new);
    }
}
