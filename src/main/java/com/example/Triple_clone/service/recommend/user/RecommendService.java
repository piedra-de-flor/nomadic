package com.example.Triple_clone.service.recommend.user;

import com.example.Triple_clone.domain.entity.Recommendation;
import com.example.Triple_clone.domain.vo.RecommendOrderType;
import com.example.Triple_clone.dto.recommend.user.RecommendReadDto;
import com.example.Triple_clone.dto.recommend.user.RecommendReadTop10Dto;
import com.example.Triple_clone.repository.RecommendationRepository;
import com.example.Triple_clone.service.support.FileManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendService {
    private final static int PAGE_SIZE = 5;
    ConcurrentHashMap<Long, ConcurrentLinkedDeque<Long>> likes = new ConcurrentHashMap<>();

    private final RecommendationRepository recommendationRepository;
    private final FileManager fileManager;


    @Transactional(readOnly = true)
    public RecommendReadDto findById(long recommendationId, long userId) {
        Recommendation recommendation = recommendationRepository.findById(recommendationId)
                .orElseThrow(() -> new NoSuchElementException("no place entity"));

        boolean likeOrNot = recommendation.isLikedBy(userId);

        return new RecommendReadDto(recommendation, likeOrNot);
    }

    public byte[] loadImageAsResource(Long recommendationId) {
        Recommendation recommendation = findById(recommendationId);
        String path = recommendation.getMainImage().getStoredFileName();
        return fileManager.loadImageAsResource(path);
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

    public List<RecommendReadTop10Dto> findTop10() {
        List<RecommendReadTop10Dto> response = new ArrayList<>();
        List<Recommendation> sortedList = recommendationRepository.findAll().stream()
                .sorted(Comparator.comparingInt(value -> value.getLikes().size()))
                .toList();

        for (int i = 0; i < 10; i++) {
            RecommendReadTop10Dto dto = new RecommendReadTop10Dto(sortedList.get(i).getId(),
                    sortedList.get(i).getMainImage(),
                    sortedList.get(i).getTitle());

            response.add(dto);
        }

        return response;
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
