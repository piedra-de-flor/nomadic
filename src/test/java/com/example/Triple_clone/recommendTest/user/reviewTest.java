package com.example.Triple_clone.recommendTest.user;

import com.example.Triple_clone.dto.recommend.user.RecommendForUserWriteReviewRequestDto;
import com.example.Triple_clone.entity.Place;
import com.example.Triple_clone.entity.User;
import com.example.Triple_clone.repository.PlaceRepository;
import com.example.Triple_clone.repository.ReviewRepository;
import com.example.Triple_clone.repository.UserRepository;
import com.example.Triple_clone.service.recommend.user.RecommendForUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class reviewTest {
    @Autowired
    PlaceRepository placeRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ReviewRepository reviewRepository;
    RecommendForUserService service;

    @BeforeEach
    void setUp() {
        service = new RecommendForUserService(placeRepository, userRepository, reviewRepository);
    }

    @Test
    void 리뷰_작성_테스트() {
        RecommendForUserWriteReviewRequestDto dto = new RecommendForUserWriteReviewRequestDto(1,1,"test","test");
        User testUser = new User();
        Place testPlace = new Place("test", "test", "test", "test", "test");

        userRepository.save(testUser);
        placeRepository.save(testPlace);

        service.writeReview(dto);

        assertThat(reviewRepository.findAll().get(0).getContent()).isEqualTo("test");
        assertThat(testPlace.getReviews().get(0).getContent()).isEqualTo("test");
    }
}
