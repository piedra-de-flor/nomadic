package com.example.Triple_clone.service.recommend.manager;

import com.example.Triple_clone.dto.recommend.manager.AdminRecommendCreatePlaceDto;
import com.example.Triple_clone.dto.recommend.manager.AdminRecommendUpdatePlaceDto;
import com.example.Triple_clone.entity.Place;
import com.example.Triple_clone.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminRecommendService {
    private final PlaceRepository repository;

    public void createPlace(AdminRecommendCreatePlaceDto createPlaceRequestDto) {
        repository.save(createPlaceRequestDto.toEntity());
    }

    public void updatePlace(AdminRecommendUpdatePlaceDto updatePlaceRequestDto) {
        Optional<Place> place = repository.findById(updatePlaceRequestDto.placeId());
        Place target = place.orElseThrow(() -> new IllegalArgumentException("no place entity for update"));

        target.update(updatePlaceRequestDto.title(),
                updatePlaceRequestDto.notionUrl(),
                updatePlaceRequestDto.subTitle(),
                updatePlaceRequestDto.location(),
                updatePlaceRequestDto.mainImage());
    }

    public void deletePlace(Long placeId) {
        Optional<Place> place = repository.findById(placeId);
        Place target = place.orElseThrow(() -> new IllegalArgumentException("no place entity for delete"));

        repository.delete(target);
    }
}
