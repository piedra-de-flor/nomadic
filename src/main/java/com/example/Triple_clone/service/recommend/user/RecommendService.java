package com.example.Triple_clone.service.recommend.user;

import com.example.Triple_clone.dto.recommend.user.RecommendReadDto;
import com.example.Triple_clone.domain.entity.Place;
import com.example.Triple_clone.repository.PlaceRepository;
import com.example.Triple_clone.domain.vo.RecommendOrderType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendService {
    private final static int PAGE_SIZE = 5;
    private final PlaceRepository placeRepository;

    @Transactional(readOnly = true)
    public RecommendReadDto findById(long placeId, long userId) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new NoSuchElementException("no place entity"));

        boolean likeOrNot = place.isLikedBy(userId);

        return new RecommendReadDto(place, likeOrNot);
    }

    public Place getById(long placeId) {
        return placeRepository.getReferenceById(placeId);
    }

    @Transactional(readOnly = true)
    public Page<RecommendReadDto> findAll(String orderType, Pageable pageable) {
        Pageable customPageable = PageRequest.of(pageable.getPageNumber(), PAGE_SIZE, Sort.by(RecommendOrderType.valueOf(orderType).property).descending());
        Page<Place> placesPage = placeRepository.findAllByOrderByTitleDesc(customPageable);

        List<RecommendReadDto> dtos = placesPage.getContent().stream()
                .map(place -> new RecommendReadDto(place, false))
                .toList();

        return new PageImpl<>(dtos, pageable, placesPage.getTotalElements());
    }

    @Transactional
    public void like(long placeId, Long userId) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new NoSuchElementException("no place entity"));

        place.like(userId);
    }
}
