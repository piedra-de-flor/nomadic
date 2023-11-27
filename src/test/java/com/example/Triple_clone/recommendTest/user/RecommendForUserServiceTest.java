package com.example.Triple_clone.recommendTest.user;

import com.example.Triple_clone.dto.recommend.user.RecommendForUserReadAllResponseDto;
import com.example.Triple_clone.dto.recommend.user.RecommendForUserReadResponseDto;
import com.example.Triple_clone.entity.Place;
import com.example.Triple_clone.repository.PlaceRepository;
import com.example.Triple_clone.repository.ReviewRepository;
import com.example.Triple_clone.repository.UserRepository;
import com.example.Triple_clone.service.recommend.user.RecommendForUserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecommendForUserServiceTest {
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

    @InjectMocks
    private RecommendForUserService service;


    @Test
    void 서비스_레이어_장소_단일_조회_테스트() {
        place1.setLikes(Collections.singletonList(1L));

        when(placeRepository.findById(1L)).thenReturn(Optional.of(place1));
        when(place1.getId()).thenReturn(1L);

        RecommendForUserReadResponseDto responseDto = service.findById(1L, 1L);

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
        place1.setLikes(Collections.singletonList(1L));

        List<Place> list = new ArrayList<>();
        list.add(place1);
        list.add(place2);

        when(placeRepository.findAll()).thenReturn(list);

        when(place1.getDate()).thenReturn(LocalDate.of(2023, 11, 1).atStartOfDay());
        when(place2.getDate()).thenReturn(LocalDate.of(2023, 10, 15).atStartOfDay());

        RecommendForUserReadAllResponseDto responseDto = service.findAll("date");

        assertThat(responseDto).isNotNull();
        assertThat(responseDto.places().size()).isEqualTo(2);
        assertThat(responseDto.places().get(0)).isEqualTo(place1);
    }

    @Test
    void 서비스_레이어_장소_전체_조회_이름순_테스트() {
        place1.setLikes(Collections.singletonList(1L));

        List<Place> list = new ArrayList<>();
        list.add(place1);
        list.add(place2);

        when(placeRepository.findAll()).thenReturn(list);

        when(place1.getTitle()).thenReturn("AAA");
        when(place2.getTitle()).thenReturn("BBB");

        RecommendForUserReadAllResponseDto responseDto = service.findAll("name");

        assertThat(responseDto).isNotNull();
        assertThat(responseDto.places().size()).isEqualTo(2);
        assertThat(responseDto.places().get(0)).isEqualTo(place1);
    }

    @Test
    void 서비스_레이어_장소_좋아요_테스트() {
        service.like(1L, 1L);
        assertAll(
                () -> verify(placeRepository, times(1)).saveLike(1L, 1L)
        );
    }

    @Test
    void 서비스_레이어_리뷰_작성_테스트() {
    }
}
