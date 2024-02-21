package com.example.Triple_clone.domain.entity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.example.Triple_clone.domain.entity.Place;
import com.example.Triple_clone.domain.entity.Review;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PlaceTest {
    @Test
    void 장소_업데이트_테스트() {
        Place place = new Place("test", "test", "test", "test", "test");
        place.update("test0", "test0", "test0", "test0", "test0");

        assertThat(place.getTitle()).isEqualTo("test0");
        assertThat(place.getLocation()).isEqualTo("test0");
        assertThat(place.getNotionUrl()).isEqualTo("test0");
        assertThat(place.getSubTitle()).isEqualTo("test0");
        assertThat(place.getMainImage()).isEqualTo("test0");
    }

    @Test
    void 장소_업데이트_실패_테스트_제목_null() {
        Place place = new Place("test", "test", "test", "test", "test");

        Assertions.assertThrows(NullPointerException.class,
                () -> place.update(null, "test0", "test0", "test0", "test0"));
    }

    @Test
    void 장소_좋아요_테스트() {
        Place place = new Place("test", "test", "test", "test", "test");
        place.like(1);
        place.like(2);
        place.like(3);

        assertThat(place.getLikesNumber()).isEqualTo(3);
    }

    @Test
    void 장소_좋아요_여부_테스트() {
        Place place = new Place("test", "test", "test", "test", "test");
        place.like(1);

        assertThat(place.isLikedBy(1)).isEqualTo(true);
        assertThat(place.isLikedBy(2)).isEqualTo(false);
    }

    @Test
    void 장소_리뷰_쓰기_테스트() {
        Place place = new Place("test", "test", "test", "test", "test");
        Review review1 = new Review();
        Review review2 = new Review();
        Review review3 = new Review();

        place.addReview(review1);
        place.addReview(review2);
        place.addReview(review3);

        assertThat(place.getReviewsNumber()).isEqualTo(3);
    }
}
