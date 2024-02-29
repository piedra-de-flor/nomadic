package com.example.Triple_clone.service.recommend.admin;

import com.example.Triple_clone.domain.vo.Image;
import com.example.Triple_clone.dto.recommend.admin.AdminRecommendCreateRecommendationDto;
import com.example.Triple_clone.dto.recommend.admin.AdminRecommendUpdateRecommendationDto;
import com.example.Triple_clone.domain.entity.Recommendation;
import com.example.Triple_clone.repository.RecommendationRepository;
import com.example.Triple_clone.service.support.FileManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminRecommendService {
    private final RecommendationRepository repository;
    private final FileManager fileManager;

    public Recommendation createRecommendation(AdminRecommendCreateRecommendationDto createRecommendationRequestDto, MultipartFile image) {
        Recommendation recommendation = createRecommendationRequestDto.toEntity();
        Image mainImage = fileManager.uploadImage(image);
        recommendation.setImage(mainImage);
        repository.save(recommendation);
        return recommendation;
    }

    @Transactional
    public Recommendation updateRecommendation(AdminRecommendUpdateRecommendationDto updateRecommendationRequestDto, MultipartFile image) {
        Recommendation recommendation = repository.findById(updateRecommendationRequestDto.placeId())
                .orElseThrow(() -> new NoSuchElementException("no place entity for update"));

        if (image != null) {
            fileManager.deleteExistingImage(recommendation.getMainImage().getStoredFileName());
            Image updateImage = fileManager.uploadImage(image);
            recommendation.setImage(updateImage);
        }

        recommendation.update(updateRecommendationRequestDto.title(),
                updateRecommendationRequestDto.notionUrl(),
                updateRecommendationRequestDto.subTitle(),
                updateRecommendationRequestDto.location());

        return recommendation;
    }

    @Transactional
    public long deleteRecommendation(Long recommendationId) {
        Recommendation recommendation = repository.findById(recommendationId)
                .orElseThrow(() -> new NoSuchElementException("no place entity for delete"));

        Image deleteTargetImage = recommendation.getMainImage();
        fileManager.deleteExistingImage(deleteTargetImage.getStoredFileName());
        repository.delete(recommendation);
        return recommendationId;
    }
}
