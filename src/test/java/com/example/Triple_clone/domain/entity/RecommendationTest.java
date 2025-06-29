package com.example.Triple_clone.domain.entity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.example.Triple_clone.domain.recommend.domain.Recommendation;
import com.example.Triple_clone.domain.review.domain.Review;
import com.example.Triple_clone.domain.plan.domain.Location;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RecommendationTest {
    @Test
    void 장소_업데이트_테스트() {
        Recommendation recommendation = new Recommendation("test", "test", "test", new Location(1D, 1D, "location"));
        recommendation.update("test0", "test0", "test0", new Location(1D, 1D, "location2"));

        assertThat(recommendation.getTitle()).isEqualTo("test0");
        assertThat(recommendation.getLocation().getName()).isEqualTo("location2");
        assertThat(recommendation.getNotionUrl()).isEqualTo("test0");
        assertThat(recommendation.getSubTitle()).isEqualTo("test0");
    }

    @Test
    void 장소_업데이트_실패_테스트_제목_null() {
        Recommendation recommendation = new Recommendation("test", "test", "test",  new Location(1D, 1D, "location"));

        Assertions.assertThrows(NullPointerException.class,
                () -> recommendation.update(null, "test0", "test0",  new Location(1D, 1D, "location1")));
    }

    @Test
    void 장소_좋아요_테스트() {
        Recommendation recommendation = new Recommendation("test", "test", "test", new Location(1D, 1D, "location"));
        recommendation.like(1);
        recommendation.like(2);
        recommendation.like(3);

        assertThat(recommendation.getLikes().size()).isEqualTo(3);
    }

    @Test
    void 장소_좋아요_여부_테스트() {
        Recommendation recommendation = new Recommendation("test", "test", "test", new Location(1D, 1D, "location"));
        recommendation.like(1);

        assertThat(recommendation.isLikedBy(1)).isEqualTo(true);
        assertThat(recommendation.isLikedBy(2)).isEqualTo(false);
    }

    @Test
    void 장소_리뷰_쓰기_테스트() {
        Recommendation recommendation = new Recommendation("test", "test", "test", new Location(1D, 1D, "location"));
        Review review1 = new Review();
        Review review2 = new Review();
        Review review3 = new Review();

        recommendation.addReview(review1);
        recommendation.addReview(review2);
        recommendation.addReview(review3);

        assertThat(recommendation.getReviews().size()).isEqualTo(3);
    }
}
