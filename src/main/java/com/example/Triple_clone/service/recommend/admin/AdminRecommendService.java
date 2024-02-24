package com.example.Triple_clone.service.recommend.admin;

import com.example.Triple_clone.dto.recommend.admin.AdminRecommendCreateRecommendationDto;
import com.example.Triple_clone.dto.recommend.admin.AdminRecommendUpdateRecommendationDto;
import com.example.Triple_clone.domain.entity.Recommendation;
import com.example.Triple_clone.repository.RecommendationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminRecommendService {
    private final RecommendationRepository repository;

    public Recommendation createRecommendation(AdminRecommendCreateRecommendationDto createRecommendationRequestDto) {
        Recommendation recommendation = createRecommendationRequestDto.toEntity();
        repository.save(recommendation);
        return recommendation;
    }

    @Transactional
    public Recommendation updateRecommendation(AdminRecommendUpdateRecommendationDto updateRecommendationRequestDto) {
        Recommendation recommendation = repository.findById(updateRecommendationRequestDto.placeId())
                .orElseThrow(() -> new NoSuchElementException("no place entity for update"));

        recommendation.update(updateRecommendationRequestDto.title(),
                updateRecommendationRequestDto.notionUrl(),
                updateRecommendationRequestDto.subTitle(),
                updateRecommendationRequestDto.location(),
                updateRecommendationRequestDto.mainImage());

        return recommendation;
    }

    @Transactional
    public long deleteRecommendation(Long recommendationId) {
        Recommendation recommendation = repository.findById(recommendationId)
                .orElseThrow(() -> new NoSuchElementException("no place entity for delete"));

        repository.delete(recommendation);
        return recommendationId;
    }
}
