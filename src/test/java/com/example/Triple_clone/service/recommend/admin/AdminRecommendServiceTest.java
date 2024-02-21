package com.example.Triple_clone.service.recommend.admin;

import com.example.Triple_clone.domain.entity.Place;
import com.example.Triple_clone.dto.recommend.admin.AdminRecommendCreatePlaceDto;
import com.example.Triple_clone.dto.recommend.admin.AdminRecommendUpdatePlaceDto;
import com.example.Triple_clone.repository.PlaceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminRecommendServiceTest {
    @Mock
    private PlaceRepository placeRepository;
    @InjectMocks
    private AdminRecommendService service;
    @Mock
    Place place;
    @Mock
    AdminRecommendCreatePlaceDto adminRecommendCreatePlaceDto;
    @Mock
    AdminRecommendUpdatePlaceDto adminRecommendUpdatePlaceDto;

    @Test
    void 서비스_레이어_관리자_장소_생성_테스트() {
        when(adminRecommendCreatePlaceDto.toEntity()).thenReturn(place);

        service.createPlace(adminRecommendCreatePlaceDto);

        verify(placeRepository, times(1)).save(place);

    }

    @Test
    void 서비스_레이어_관리자_장소_수정_테스트() {
        when(adminRecommendUpdatePlaceDto.placeId()).thenReturn(1L);
        when(placeRepository.findById(1L)).thenReturn(Optional.ofNullable(place));

        service.updatePlace(adminRecommendUpdatePlaceDto);


        verify(place, times(1)).update(null, null, null, null, null);

    }

    @Test
    void 서비스_레이어_관리자_장소_삭제_테스트() {
        when(placeRepository.findById(1L)).thenReturn(Optional.ofNullable(place));

        service.deletePlace(1L);


        verify(placeRepository, times(1)).delete(place);

    }
}
