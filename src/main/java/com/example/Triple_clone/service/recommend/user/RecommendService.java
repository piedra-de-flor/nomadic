package com.example.Triple_clone.service.recommend.user;

import com.example.Triple_clone.domain.entity.Place;
import com.example.Triple_clone.domain.vo.RecommendOrderType;
import com.example.Triple_clone.dto.recommend.user.RecommendReadDto;
import com.example.Triple_clone.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendService {
    private final static int PAGE_SIZE = 5;
    ConcurrentHashMap<Long, ConcurrentLinkedDeque<Long>> likes = new ConcurrentHashMap<>();

    private final PlaceRepository placeRepository;


    @Transactional(readOnly = true)
    public RecommendReadDto findById(long placeId, long userId) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new NoSuchElementException("no place entity"));

        boolean likeOrNot = place.isLikedBy(userId);

        return new RecommendReadDto(place, likeOrNot);
    }

    public Place findById(long placeId) {
        return placeRepository.findById(placeId)
                .orElseThrow(() -> new NoSuchElementException("no place entity"));
    }

    @Transactional(readOnly = true)
    public Page<RecommendReadDto> findAll(String orderType, Pageable pageable) {
        Page<Place> placesPage;
        Pageable customPageable = PageRequest.of(pageable.getPageNumber(), PAGE_SIZE, Sort.by(RecommendOrderType.valueOf(orderType).property).descending());

        if (RecommendOrderType.valueOf(orderType).equals(RecommendOrderType.name)) {
            placesPage = placeRepository.findAllByOrderByTitleDesc(customPageable);
        } else {
            placesPage = placeRepository.findAllByOrderByDateDesc(customPageable);
        }

        List<RecommendReadDto> dtos = placesPage.getContent().stream()
                .map(place -> new RecommendReadDto(place, false))
                .toList();

        return new PageImpl<>(dtos, pageable, placesPage.getTotalElements());
    }

    public void like(Long placeId, Long userId) {
        if (likes.containsKey(placeId)) {
            if (likes.get(placeId).contains(userId)) {
                likes.get(placeId).remove(userId);
            } else {
                likes.get(placeId).add(userId);
            }
        } else {
            likes.put(placeId, new ConcurrentLinkedDeque<>());
            likes.get(placeId).add(userId);
        }
    }

    @Scheduled(fixedRate = 5000)
    @Transactional
    public void saveLike() {
        if (!likes.isEmpty()) {
            likes.forEach((placeId, userIds) -> {
                Place target = placeRepository.findById(placeId)
                        .orElseThrow(NoSuchElementException::new);
                userIds.forEach(target::like);
            });
            likes.clear();
        }
    }
}
