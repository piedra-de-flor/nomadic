package com.example.Triple_clone.service.recommend.admin;

import com.example.Triple_clone.domain.recommend.domain.Recommendation;
import com.example.Triple_clone.domain.recommend.application.RecommendCommandService;
import com.example.Triple_clone.common.file.Image;
import com.example.Triple_clone.domain.recommend.web.dto.RecommendationCreateDto;
import com.example.Triple_clone.domain.recommend.web.dto.RecommendationUpdateDto;
import com.example.Triple_clone.domain.recommend.infra.RecommendationRepository;
import com.example.Triple_clone.common.file.FileManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminRecommendQueryServiceTest {
    @Mock
    private RecommendationRepository recommendationRepository;
    @InjectMocks
    private RecommendCommandService service;
    @Mock
    Recommendation recommendation;
    @Mock
    FileManager fileManager;
    @Mock
    RecommendationCreateDto recommendationCreateDto;
    @Mock
    RecommendationUpdateDto recommendationUpdateDto;

    @Test
    void 서비스_레이어_관리자_장소_생성_테스트() {
        when(recommendationCreateDto.toEntity()).thenReturn(recommendation);

        service.createRecommendation(recommendationCreateDto);

        verify(recommendationRepository, times(1)).save(recommendation);

    }

    @Test
    void 서비스_레이어_관리자_장소_수정_테스트() {
        when(recommendationUpdateDto.placeId()).thenReturn(1L);
        when(recommendationRepository.findById(1L)).thenReturn(Optional.ofNullable(recommendation));

        service.updateRecommendation(recommendationUpdateDto);


        verify(recommendation, times(1)).update(null, null, null, null);

    }

    @Test
    void 서비스_레이어_관리자_장소_삭제_테스트() {
        Image image = mock(Image.class);
        when(recommendationRepository.findById(1L)).thenReturn(Optional.ofNullable(recommendation));
        when(recommendation.getMainImage()).thenReturn(image);
        when(image.getStoredFileName()).thenReturn(anyString());

        service.deleteRecommendation(1L);


        verify(recommendationRepository, times(1)).delete(recommendation);
    }
}
