package com.example.Triple_clone.service.recommend.admin;

import com.example.Triple_clone.dto.recommend.admin.AdminRecommendCreatePlaceDto;
import com.example.Triple_clone.dto.recommend.admin.AdminRecommendUpdatePlaceDto;
import com.example.Triple_clone.domain.entity.Recommendation;
import com.example.Triple_clone.repository.PlaceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminRecommendService {
    private final PlaceRepository repository;

    public Recommendation createPlace(AdminRecommendCreatePlaceDto createPlaceRequestDto) {
        Recommendation recommendation = createPlaceRequestDto.toEntity();
        repository.save(recommendation);
        return recommendation;
    }

    @Transactional
    public Recommendation updatePlace(AdminRecommendUpdatePlaceDto updatePlaceRequestDto) {
        Recommendation recommendation = repository.findById(updatePlaceRequestDto.placeId())
                .orElseThrow(() -> new NoSuchElementException("no place entity for update"));

        recommendation.update(updatePlaceRequestDto.title(),
                updatePlaceRequestDto.notionUrl(),
                updatePlaceRequestDto.subTitle(),
                updatePlaceRequestDto.location(),
                updatePlaceRequestDto.mainImage());

        return recommendation;
    }

    @Transactional
    public long deletePlace(Long placeId) {
        Recommendation recommendation = repository.findById(placeId)
                .orElseThrow(() -> new NoSuchElementException("no place entity for delete"));

        repository.delete(recommendation);
        return placeId;
    }
}
