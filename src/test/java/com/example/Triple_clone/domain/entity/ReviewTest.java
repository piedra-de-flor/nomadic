package com.example.Triple_clone.domain.entity;

import com.example.Triple_clone.domain.member.domain.Member;
import com.example.Triple_clone.domain.recommend.domain.Recommendation;
import com.example.Triple_clone.domain.review.domain.Review;
import com.example.Triple_clone.common.file.Image;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReviewTest {

    @Test
    void 리뷰_객체_생성_및_조회() {
        // given
        Member member = new Member();
        Recommendation recommendation = new Recommendation();
        String content = "좋은 장소였어요!";

        // when
        Review review = new Review(member, recommendation, content);

        // then
        assertThat(review.getMember()).isEqualTo(member);
        assertThat(review.getRecommendation()).isEqualTo(recommendation);
        assertThat(review.getContent()).isEqualTo(content);
    }

    @Test
    void 이미지_설정_테스트() {
        // given
        Review review = new Review(new Member(), new Recommendation(), "테스트");
        Image image = new Image("original.jpg", "stored.jpg");

        // when
        String storedFileName = review.setImage(image);

        // then
        assertThat(review.getImage()).isEqualTo(image);
        assertThat(storedFileName).isEqualTo("stored.jpg");
    }
}
