package com.example.Triple_clone.service.recommend.manager;

import com.example.Triple_clone.dto.recommend.manager.RecommendForManagerCreatePlaceRequestDto;
import com.example.Triple_clone.dto.recommend.manager.RecommendForManagerUpdatePlaceRequestDto;
import com.example.Triple_clone.entity.Place;
import com.example.Triple_clone.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecommendForManagerService {
    private final PlaceRepository repository;

    public void createPlace(RecommendForManagerCreatePlaceRequestDto createPlaceRequestDto) {
        repository.save(createPlaceRequestDto.toEntity());
    }

    public void updatePlace(RecommendForManagerUpdatePlaceRequestDto updatePlaceRequestDto) {
        Optional<Place> place = repository.findById(updatePlaceRequestDto.placeId());
        Place target = place.orElseThrow(() -> new IllegalArgumentException("no place entity for update"));

        target.update(updatePlaceRequestDto.title(),
                updatePlaceRequestDto.notionUrl(),
                updatePlaceRequestDto.subTitle(),
                updatePlaceRequestDto.location(),
                updatePlaceRequestDto.mainImage());
    }
}
