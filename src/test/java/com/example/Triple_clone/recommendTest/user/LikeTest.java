package com.example.Triple_clone.recommendTest.user;

import com.example.Triple_clone.entity.Place;
import com.example.Triple_clone.entity.User;
import com.example.Triple_clone.repository.PlaceRepository;
import com.example.Triple_clone.repository.ReviewRepository;
import com.example.Triple_clone.repository.UserRepository;
import com.example.Triple_clone.service.recommend.user.RecommendService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class LikeTest {
    @Autowired
    PlaceRepository placeRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ReviewRepository reviewRepository;
    RecommendService service;

    Place testPlace = new Place("test", "test", "test", "test", "test");
    @Mock
    User testUser;

    @BeforeEach
    void setUp() {
        service = new RecommendService(placeRepository, userRepository, reviewRepository);
    }


    @Test
    void 추천_장소_찜_개수_증가_테스트() {
        userRepository.save(testUser);
        placeRepository.save(testPlace);

        service.like(testPlace.getId(), testUser.getId());
        assertThat(testPlace.getLikes().size()).isEqualTo(1);
    }

    @Test
    void 추천_장소_찜_취소_테스트() {
        userRepository.save(testUser);
        placeRepository.save(testPlace);

        service.like(testPlace.getId(), testUser.getId());
        assertThat(testPlace.getLikes().size()).isEqualTo(1);

        service.like(testPlace.getId(), testUser.getId());
        assertThat(testPlace.getLikes().size()).isEqualTo(0);
    }
}
