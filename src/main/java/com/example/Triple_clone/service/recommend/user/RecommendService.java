package com.example.Triple_clone.service.recommend.user;

import com.example.Triple_clone.domain.entity.Recommendation;
import com.example.Triple_clone.domain.vo.RecommendOrderType;
import com.example.Triple_clone.dto.recommend.user.RecommendReadDto;
import com.example.Triple_clone.repository.RecommendationRepository;
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

    private final RecommendationRepository recommendationRepository;


    @Transactional(readOnly = true)
    public RecommendReadDto findById(long recommendationId, long userId) {
        Recommendation recommendation = recommendationRepository.findById(recommendationId)
                .orElseThrow(() -> new NoSuchElementException("no place entity"));

        boolean likeOrNot = recommendation.isLikedBy(userId);

        return new RecommendReadDto(recommendation, likeOrNot);
    }

    public Recommendation findById(long recommendationId) {
        return recommendationRepository.getReferenceById(recommendationId);
    }

    @Transactional(readOnly = true)
    public Page<RecommendReadDto> findAll(String orderType, Pageable pageable) {
        Page<Recommendation> placesPage;
        Pageable customPageable = PageRequest.of(pageable.getPageNumber(), PAGE_SIZE, Sort.by(RecommendOrderType.valueOf(orderType).property).descending());

        if (RecommendOrderType.valueOf(orderType).equals(RecommendOrderType.title)) {
            placesPage = recommendationRepository.findAllByOrderByTitleDesc(customPageable);
        } else {
            placesPage = recommendationRepository.findAllByOrderByDateDesc(customPageable);
        }

        List<RecommendReadDto> dtos = placesPage.getContent().stream()
                .map(place -> new RecommendReadDto(place, false))
                .toList();

        return new PageImpl<>(dtos, pageable, placesPage.getTotalElements());
    }

    public void like(Long recommendationId, Long userId) {
        if (likes.containsKey(recommendationId)) {
            if (likes.get(recommendationId).contains(userId)) {
                likes.get(recommendationId).remove(userId);
            } else {
                likes.get(recommendationId).add(userId);
            }
        } else {
            likes.put(recommendationId, new ConcurrentLinkedDeque<>());
            likes.get(recommendationId).add(userId);
        }
    }

    @Scheduled(fixedRate = 5000)
    @Transactional
    public void saveLike() {
        if (!likes.isEmpty()) {
            likes.forEach((placeId, userIds) -> {
                Recommendation target = recommendationRepository.findById(placeId)
                        .orElseThrow(NoSuchElementException::new);
                userIds.forEach(target::like);
            });
            likes.clear();
        }
    }
}
