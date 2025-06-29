package com.example.Triple_clone.domain.recommend.application;

import com.example.Triple_clone.common.file.Image;
import com.example.Triple_clone.common.file.FileManager;
import com.example.Triple_clone.domain.recommend.domain.Recommendation;
import com.example.Triple_clone.domain.recommend.infra.RecommendationRepository;
import com.example.Triple_clone.domain.recommend.web.dto.AdminRecommendCreateRecommendationDto;
import com.example.Triple_clone.domain.recommend.web.dto.AdminRecommendUpdateRecommendationDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminRecommendService {
    private final RecommendationRepository repository;
    private final FileManager fileManager;

    public Recommendation createRecommendation(AdminRecommendCreateRecommendationDto createRecommendationRequestDto) {
        Recommendation recommendation = createRecommendationRequestDto.toEntity();
        repository.save(recommendation);
        return recommendation;
    }

    @Transactional
    public Long setMainImageOfRecommendation(Long recommendationId, MultipartFile image) {
        Recommendation recommendation = repository.findById(recommendationId)
                .orElseThrow(() -> {
                    log.warn("⚠️ 추천 장소 이미지 삽입 실패 - 존재하지 않는 추천 장소: {}", recommendationId);
                    return new EntityNotFoundException("no place entity for update");
                });
        Image mainImage = fileManager.uploadImage(image);
        recommendation.setImage(mainImage);
        return recommendationId;
    }

    @Transactional
    public Recommendation updateRecommendation(AdminRecommendUpdateRecommendationDto updateRecommendationRequestDto) {
        Recommendation recommendation = repository.findById(updateRecommendationRequestDto.placeId())
                .orElseThrow(() -> {
                    log.warn("⚠️ 추천 장소 컨텐츠 수정 실패 - 존재하지 않는 추천 장소: {}", updateRecommendationRequestDto.placeId());
                    return new EntityNotFoundException("no place entity for update");
                });

        recommendation.update(updateRecommendationRequestDto.title(),
                updateRecommendationRequestDto.notionUrl(),
                updateRecommendationRequestDto.subTitle(),
                updateRecommendationRequestDto.location());

        return recommendation;
    }

    @Transactional
    public long deleteRecommendation(Long recommendationId) {
        Recommendation recommendation = repository.findById(recommendationId)
                .orElseThrow(() -> {
                    log.warn("⚠️ 추천 장소 삭제 실패 - 존재하지 않는 추천 장소: {}", recommendationId);
                    return new EntityNotFoundException("no place entity for delete");
                });

        repository.delete(recommendation);
        fileManager.deleteExistingImage(recommendation.getMainImage().getStoredFileName());
        return recommendationId;
    }
}
