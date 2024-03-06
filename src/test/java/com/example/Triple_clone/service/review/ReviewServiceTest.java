package com.example.Triple_clone.service.review;

import com.example.Triple_clone.domain.entity.Recommendation;
import com.example.Triple_clone.domain.entity.Review;
import com.example.Triple_clone.domain.entity.Member;
import com.example.Triple_clone.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReviewServiceTest {
    @MockBean
    ReviewService reviewService;
    @Mock
    Member member;
    @Mock
    Recommendation recommendation;

    @Test
    void 리뷰_저장_테스트() {
        ReviewRepository mockRepository = mock(ReviewRepository.class);
        ReviewService reviewService = new ReviewService(mockRepository);
        Review review = new Review(member, recommendation, "test");

        reviewService.save(review);
        verify(mockRepository, Mockito.times(1)).save(review);

        ArgumentCaptor<Review> reviewCaptor = ArgumentCaptor.forClass(Review.class);
        verify(mockRepository).save(reviewCaptor.capture());

        assertEquals(review, reviewCaptor.getValue());
    }
}
