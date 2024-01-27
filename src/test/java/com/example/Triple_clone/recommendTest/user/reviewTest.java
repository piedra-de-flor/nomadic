/*
package com.example.Triple_clone.recommendTest.user;

import com.example.Triple_clone.dto.recommend.user.RecommendWriteReviewDto;
import com.example.Triple_clone.domain.entity.Place;
import com.example.Triple_clone.domain.entity.User;
import com.example.Triple_clone.repository.PlaceRepository;
import com.example.Triple_clone.repository.ReviewRepository;
import com.example.Triple_clone.repository.UserRepository;
import com.example.Triple_clone.service.recommend.user.RecommendService;
import com.example.Triple_clone.domain.vo.Role;
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
    RecommendService service;

    @BeforeEach
    void setUp() {
        service = new RecommendService(placeRepository, userRepository, reviewRepository);
        userRepository.deleteAll();
        placeRepository.deleteAll();
    }

    */
/*@Test
    void 리뷰_작성_테스트() {
        User testUser = User.builder()
                .name("test")
                .email("test")
                .password("test")
                .role(Role.ADMIN)
                .build();
        Place testPlace = new Place("test", "test", "test", "test", "test");

        userRepository.save(testUser);
        placeRepository.save(testPlace);

        RecommendWriteReviewDto dto = new RecommendWriteReviewDto(testUser.getId(), testPlace.getId(), "test", "test");


        service.writeReview(dto);

        assertThat(reviewRepository.findAll().get(0).getContent()).isEqualTo("test");
        assertThat(testPlace.getReviews().get(0).getContent()).isEqualTo("test");
    }*//*

}
*/
