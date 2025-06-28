package com.example.Triple_clone.domain.recommend;

import com.example.Triple_clone.domain.member.Member;
import com.example.Triple_clone.domain.member.MemberRepository;
import com.example.Triple_clone.common.FileManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendService {
    private final static int PAGE_SIZE = 5;
    ConcurrentHashMap<Long, ConcurrentLinkedDeque<Long>> likes = new ConcurrentHashMap<>();

    private final RecommendationRepository recommendationRepository;
    private final MemberRepository memberRepository;
    private final FileManager fileManager;


    @Transactional(readOnly = true)
    public RecommendReadDto findById(long recommendationId, String email) {
        Recommendation recommendation = recommendationRepository.findById(recommendationId)
                .orElseThrow(() -> {
                    log.warn("⚠️ 추천 장소 조회 실패 - 존재하지 않는 추천 장소: {}", recommendationId);
                    return new EntityNotFoundException("no place entity");
                });

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("⚠️ 사용자 조회 실패 - 존재하지 않는 회원: {}", email);
                    return new EntityNotFoundException("no user entity");
                });

        boolean likeOrNot = recommendation.isLikedBy(member.getId());

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
                        .orElseThrow(() -> {
                            log.warn("⚠️ 추천 장소 조회 실패 - 존재하지 않는 추천 장소: {}", placeId);
                            return new EntityNotFoundException("no place entity for like");
                        });
                userIds.forEach(target::like);
            });
            likes.clear();
        }
    }
}
