package com.example.Triple_clone.service.recommend.admin;

import com.example.Triple_clone.dto.recommend.admin.AdminRecommendCreatePlaceDto;
import com.example.Triple_clone.dto.recommend.admin.AdminRecommendUpdatePlaceDto;
import com.example.Triple_clone.domain.entity.Place;
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

    public Place createPlace(AdminRecommendCreatePlaceDto createPlaceRequestDto) {
        Place place = createPlaceRequestDto.toEntity();
        repository.save(place);
        return place;
    }

    @Transactional
    public Place updatePlace(AdminRecommendUpdatePlaceDto updatePlaceRequestDto) {
        Place place = repository.findById(updatePlaceRequestDto.placeId())
                .orElseThrow(() -> new NoSuchElementException("no place entity for update"));

        place.update(updatePlaceRequestDto.title(),
                updatePlaceRequestDto.notionUrl(),
                updatePlaceRequestDto.subTitle(),
                updatePlaceRequestDto.location(),
                updatePlaceRequestDto.mainImage());

        return place;
    }

    @Transactional
    public long deletePlace(Long placeId) {
        Place place = repository.findById(placeId)
                .orElseThrow(() -> new NoSuchElementException("no place entity for delete"));

        repository.delete(place);
        return placeId;
    }
}
