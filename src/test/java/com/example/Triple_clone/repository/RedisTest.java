package com.example.Triple_clone.repository;

import com.example.Triple_clone.domain.entity.RedisAccommodation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataRedisTest
public class RedisTest {

    @Autowired
    private RedisAccommodationRepository redisAccommodationRepository;

    private RedisAccommodation testAccommodation;

    @BeforeEach
    void setUp() {
        testAccommodation = new RedisAccommodation(
                1L, "Seoul", "Test Hotel", 4.5, "Hotel",
                3, 50000, true, LocalTime.of(14, 0),
                10, 100000, 90000
        );
        redisAccommodationRepository.save(testAccommodation);
    }

    @AfterEach
    void tearDown() {
        redisAccommodationRepository.deleteAll();
    }

    @Test
    void 숙소_저장_및_조회_테스트() {
        // given
        Long id = testAccommodation.getId();

        // when
        Optional<RedisAccommodation> found = redisAccommodationRepository.findById(id);

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Test Hotel");
        assertThat(found.get().getScore()).isEqualTo(4.5);
    }

    @Test
    void 숙소_삭제_테스트() {
        // given
        Long id = testAccommodation.getId();

        // when
        redisAccommodationRepository.deleteById(id);
        Optional<RedisAccommodation> found = redisAccommodationRepository.findById(id);

        // then
        assertThat(found).isEmpty();
    }
}
