package com.example.Triple_clone.service.recommend.user;

import com.example.Triple_clone.dto.recommend.user.RecommendReadDto;
import com.example.Triple_clone.domain.entity.Place;
import com.example.Triple_clone.repository.PlaceRepository;
import com.example.Triple_clone.domain.vo.RecommendOrderType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendService {
    private final static int PAGE_SIZE = 5;
    Map<Place, List<Long>> likes = new HashMap<>();

    private final PlaceRepository placeRepository;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

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

    @Transactional
    public void like(Long placeId, Long userId) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new NoSuchElementException("no place entity"));

        List<Long> temp = place.getLikes();
        likes.put(place, temp);

        if (likes.get(place).contains(userId)) {
            likes.get(place).remove(userId);
        } else {
            likes.get(place).add(userId);
        }

        //scheduleSaveLike();
    }

    private void scheduleSaveLike() {
        scheduler.schedule(this::saveLike, 5, TimeUnit.SECONDS);
    }

    public void saveLike() {
        Set<Place> places = likes.keySet();

        for (Place place : places) {
            place.updateLike(likes.get(place));
        }

        log.info("save like");
        likes.clear();
    }
}
