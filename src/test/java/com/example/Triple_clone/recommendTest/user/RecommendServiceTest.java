package com.example.Triple_clone.recommendTest.user;

import com.example.Triple_clone.dto.recommend.user.RecommendReadDto;
import com.example.Triple_clone.dto.recommend.user.RecommendWriteReviewDto;
import com.example.Triple_clone.entity.Place;
import com.example.Triple_clone.entity.Review;
import com.example.Triple_clone.entity.User;
import com.example.Triple_clone.repository.PlaceRepository;
import com.example.Triple_clone.repository.ReviewRepository;
import com.example.Triple_clone.repository.UserRepository;
import com.example.Triple_clone.service.recommend.user.RecommendService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecommendServiceTest {
    @Mock
    private PlaceRepository placeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private Place place1;

    @Mock
    private Place place2;

    @Mock
    private User user1;

    @InjectMocks
    private RecommendService service;

    @Mock
    Pageable pageable;


    @Test
    void 서비스_레이어_장소_단일_조회_테스트() {
        when(placeRepository.findById(1L)).thenReturn(Optional.of(place1));
        when(place1.getId()).thenReturn(1L);

        RecommendReadDto responseDto = service.findById(1L, 1L);

        assertThat(responseDto).isNotNull();
        assertThat(responseDto.id()).isEqualTo(place1.getId());
    }

    @Test
    void 서비스_레이어_장소_단일_조회_실패_테스트() {
        when(placeRepository.findById(2L)).thenReturn(Optional.empty());
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.findById(2L, 1L));
    }

    @Test
    void 서비스_레이어_장소_전체_조회_날짜순_테스트() {
        List<Place> list = new ArrayList<>();
        list.add(place1);
        list.add(place2);
        Page<Place> page = new PageImpl<>(list, PageRequest.of(0, list.size()), list.size());

        Pageable customPageable = PageRequest.of(pageable.getPageNumber(), 5, Sort.by("date").descending());
        when(placeRepository.findAllByOrderByDateDesc(customPageable)).thenReturn(page);

        when(place1.getDate()).thenReturn(LocalDateTime.of(2023, 11, 1, 0, 0));
        when(place2.getDate()).thenReturn(LocalDateTime.of(2023, 10, 15, 0, 0));

        Page<RecommendReadDto> responsePage = service.findAll("date", pageable);

        assertThat(responsePage).isNotNull();
        assertThat(responsePage.getContent().size()).isEqualTo(2);
        assertThat(responsePage.getContent().get(0).date()).isEqualTo(place1.getDate());
    }

    @Test
    void 서비스_레이어_장소_전체_조회_이름순_테스트() {
        List<Place> list = new ArrayList<>();
        list.add(place1);
        list.add(place2);
        Page<Place> page = new PageImpl<>(list, PageRequest.of(0, list.size()), list.size());
        Pageable customPageable = PageRequest.of(pageable.getPageNumber(), 5, Sort.by("title").descending());

        when(placeRepository.findAllByOrderByTitleDesc(customPageable)).thenReturn(page);

        when(place1.getTitle()).thenReturn("AAA");
        when(place2.getTitle()).thenReturn("BBB");

        Page<RecommendReadDto> responsePage = service.findAll("name", pageable);

        assertThat(responsePage).isNotNull();
        assertThat(responsePage.getContent().size()).isEqualTo(2);
        assertThat(responsePage.getContent().get(0).title()).isEqualTo(place1.getTitle());
    }


    @Test
    void 서비스_레이어_장소_좋아요_테스트() {
        service.like(1L, 1L);
        assertAll(
                () -> verify(service, times(1)).like(1L, 1L)
        );
    }

    @Test
    void 서비스_레이어_리뷰_작성_테스트() {
        RecommendWriteReviewDto dto = new RecommendWriteReviewDto(1L, 1L, "test", "test");
        when(placeRepository.findById(1L)).thenReturn(Optional.of(place1));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));

        service.writeReview(dto);

        assertAll(
                () -> verify(reviewRepository, times(1)).save(any(Review.class))
        );
    }
}
