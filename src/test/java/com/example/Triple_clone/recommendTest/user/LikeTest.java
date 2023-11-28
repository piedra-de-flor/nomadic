package com.example.Triple_clone.recommendTest.user;

import com.example.Triple_clone.entity.Place;
import com.example.Triple_clone.entity.User;
import com.example.Triple_clone.repository.PlaceRepository;
import com.example.Triple_clone.repository.ReviewRepository;
import com.example.Triple_clone.repository.UserRepository;
import com.example.Triple_clone.service.recommend.user.RecommendForUserService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

    @Autowired
    EntityManager em;
    RecommendForUserService service;

    Place testPlace = new Place("test", "test", "test", "test", "test");
    User testUser = new User();

    @BeforeEach
    void setUp() {
        service = new RecommendForUserService(placeRepository, userRepository, reviewRepository);
    }

    @Test
    void 추천_장소_찜_개수_증가_테스트() {
        userRepository.save(testUser);
        placeRepository.save(testPlace);

        service.like(1, (long)1);
        System.out.println(placeRepository.findAll().get(0).getId());
        assertThat(placeRepository.findById((long)1).get().getLikes().size()).isEqualTo(1);
    }

    @Test
    void 추천_장소_찜_취소_테스트() {
        userRepository.save(testUser);
        placeRepository.save(testPlace);

        service.like(2, (long)1);
        System.out.println(placeRepository.findAll().get(0).getId());
        assertThat(placeRepository.findById((long)2).get().getLikes().size()).isEqualTo(1);

        service.like(2, (long)1);
        System.out.println(placeRepository.findAll().get(0).getId());
        assertThat(placeRepository.findById((long)2).get().getLikes().size()).isEqualTo(0);
    }
}
