package com.example.Triple_clone.service.recommend.admin;

import com.example.Triple_clone.domain.entity.Recommendation;
import com.example.Triple_clone.dto.recommend.admin.AdminRecommendCreateRecommendationDto;
import com.example.Triple_clone.dto.recommend.admin.AdminRecommendUpdateRecommendationDto;
import com.example.Triple_clone.repository.RecommendationRepository;
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
    private RecommendationRepository recommendationRepository;
    @InjectMocks
    private AdminRecommendService service;
    @Mock
    Recommendation recommendation;
    @Mock
    AdminRecommendCreateRecommendationDto adminRecommendCreateRecommendationDto;
    @Mock
    AdminRecommendUpdateRecommendationDto adminRecommendUpdateRecommendationDto;

    @Test
    void 서비스_레이어_관리자_장소_생성_테스트() {
        when(adminRecommendCreateRecommendationDto.toEntity()).thenReturn(recommendation);

        service.createRecommendation(adminRecommendCreateRecommendationDto);

        verify(recommendationRepository, times(1)).save(recommendation);

    }

    @Test
    void 서비스_레이어_관리자_장소_수정_테스트() {
        when(adminRecommendUpdateRecommendationDto.placeId()).thenReturn(1L);
        when(recommendationRepository.findById(1L)).thenReturn(Optional.ofNullable(recommendation));

        service.updateRecommendation(adminRecommendUpdateRecommendationDto);


        verify(recommendation, times(1)).update(null, null, null, null, null);

    }

    @Test
    void 서비스_레이어_관리자_장소_삭제_테스트() {
        when(recommendationRepository.findById(1L)).thenReturn(Optional.ofNullable(recommendation));

        service.deleteRecommendation(1L);


        verify(recommendationRepository, times(1)).delete(recommendation);

    }
}
