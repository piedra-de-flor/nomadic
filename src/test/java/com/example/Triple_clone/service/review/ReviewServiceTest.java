package com.example.Triple_clone.service.review;

import com.example.Triple_clone.domain.entity.Place;
import com.example.Triple_clone.domain.entity.Review;
import com.example.Triple_clone.domain.entity.User;
import com.example.Triple_clone.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;


public class ReviewServiceTest {
    @MockBean
    ReviewService reviewService;
    @Mock
    ReviewRepository reviewRepository;
    @Mock
    User user;
    @Mock
    Place place;

    @Test
    void 리뷰_저장_테스트() {
        reviewService = new ReviewService(reviewRepository);
        Review review = new Review(user, place, "test", "test");

        reviewService.save(review);
        verify(reviewService, Mockito.times(1)).save(review);

        ArgumentCaptor<Review> reviewCaptor = ArgumentCaptor.forClass(Review.class);
        verify(reviewService).save(reviewCaptor.capture());

        assertEquals(review, reviewCaptor.getValue());
    }
}
