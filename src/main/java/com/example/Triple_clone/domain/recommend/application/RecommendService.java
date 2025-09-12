package com.example.Triple_clone.domain.recommend.application;

import com.example.Triple_clone.common.logging.logMessage.MemberLogMessage;
import com.example.Triple_clone.common.logging.logMessage.RecommendLogMessage;
import com.example.Triple_clone.domain.member.domain.Member;
import com.example.Triple_clone.domain.member.infra.MemberRepository;
import com.example.Triple_clone.common.file.FileManager;
import com.example.Triple_clone.domain.recommend.web.dto.RecommendReadDto;
import com.example.Triple_clone.domain.recommend.web.dto.RecommendReadTop10Dto;
import com.example.Triple_clone.domain.recommend.domain.Recommendation;
import com.example.Triple_clone.domain.recommend.domain.RecommendationLike;
import com.example.Triple_clone.domain.recommend.domain.RecommendationType;
import com.example.Triple_clone.domain.recommend.infra.RecommendationRepository;
import com.example.Triple_clone.domain.recommend.infra.RecommendationLikeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendService {
    private final static int PAGE_SIZE = 5;
    // 메모리에서 좋아요 상태를 추적 (추천장소ID -> 사용자ID들의 Set)
    // true: 좋아요 추가, false: 좋아요 제거
    ConcurrentHashMap<Long, ConcurrentHashMap<Long, Boolean>> likes = new ConcurrentHashMap<>();

    private final RecommendationRepository recommendationRepository;
    private final RecommendationLikeRepository likeRepository;
    private final MemberRepository memberRepository;
    private final FileManager fileManager;


    @Transactional
    public RecommendReadDto findById(long recommendationId, String email) {
        Recommendation recommendation = recommendationRepository.findById(recommendationId)
                .orElseThrow(() -> {
                    log.warn(RecommendLogMessage.RECOMMEND_SEARCH_FAILED.format("추천 장소 조회 실패", recommendationId));
                    return new EntityNotFoundException("no place entity");
                });

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn(MemberLogMessage.MEMBER_SEARCH_FAILED_BY_EMAIL.format(email));
                    return new EntityNotFoundException("no user entity");
                });

        // 조회수 증가
        recommendation.increaseViews();

        boolean likeOrNot = recommendation.isLikedBy(member.getId());

        return new RecommendReadDto(recommendation, likeOrNot);
    }

    public byte[] loadImageAsResource(Long recommendationId) {
        Recommendation recommendation = findById(recommendationId);
        
        if (recommendation.getMainImage() == null) {
            log.error("메인 이미지가 null입니다. ID: {}", recommendationId);
            throw new IllegalArgumentException("추천 장소에 메인 이미지가 없습니다. ID: " + recommendationId);
        }
        
        String path = recommendation.getMainImage().getStoredFileName();
        
        if (path == null || path.trim().isEmpty()) {
            log.error("storedFileName이 null이거나 비어있습니다. ID: {}", recommendationId);
            throw new IllegalArgumentException("이미지 파일 경로가 올바르지 않습니다. ID: " + recommendationId);
        }

        return fileManager.loadImageAsResource(path);
    }

    public Recommendation findById(long recommendationId) {
        return recommendationRepository.getReferenceById(recommendationId);
    }

    @Transactional(readOnly = true)
    public Page<RecommendReadDto> findAll(String orderType, Pageable pageable) {
        Page<Recommendation> placesPage;
        Pageable customPageable = PageRequest.of(pageable.getPageNumber(), PAGE_SIZE, 
                Sort.by(RecommendOrderType.valueOf(orderType).getDirection(), 
                       RecommendOrderType.valueOf(orderType).getField()));

        RecommendOrderType order = RecommendOrderType.valueOf(orderType);
        switch (order) {
            case CREATED_DESC, CREATED_ASC -> 
                placesPage = recommendationRepository.findAllByOrderByCreatedAtDesc(customPageable);
            case LIKES_DESC, LIKES_ASC -> 
                placesPage = recommendationRepository.findAllByOrderByLikesCountDesc(customPageable);
            case VIEWS_DESC, VIEWS_ASC -> 
                placesPage = recommendationRepository.findAllByOrderByViewsCountDesc(customPageable);
            case REVIEWS_DESC, REVIEWS_ASC -> 
                placesPage = recommendationRepository.findAllByOrderByReviewsCountDesc(customPageable);
            default -> 
                placesPage = recommendationRepository.findAllByOrderByTitleDesc(customPageable);
        }

        List<RecommendReadDto> dtos = placesPage.getContent().stream()
                .map(place -> new RecommendReadDto(place, false))
                .toList();

        return new PageImpl<>(dtos, pageable, placesPage.getTotalElements());
    }

    public List<RecommendReadTop10Dto> findTop10() {
        List<RecommendReadTop10Dto> response = new ArrayList<>();
        List<Recommendation> sortedList = recommendationRepository.findAll().stream()
                .sorted(Comparator.comparingInt(Recommendation::getLikesCount).reversed())
                .limit(10)
                .toList();

        for (Recommendation recommendation : sortedList) {
            RecommendReadTop10Dto dto = new RecommendReadTop10Dto(recommendation.getId(),
                    recommendation.getMainImage(),
                    recommendation.getTitle());
            response.add(dto);
        }

        return response;
    }

    public void toggleLike(Long recommendationId, Long memberId) {
        likes.compute(recommendationId, (rid, userMap) -> {
            if (userMap == null) userMap = new ConcurrentHashMap<>();
            userMap.compute(memberId, (uid, prev) -> (prev == null) ? Boolean.TRUE : null);
            return userMap.isEmpty() ? null : userMap;
        });
    }

    @Transactional(readOnly = true)
    public List<RecommendationLike> getLikesByRecommendationId(Long recommendationId) {
        return likeRepository.findByRecommendationId(recommendationId);
    }

    @Transactional(readOnly = true)
    public boolean isLikedByUser(Long recommendationId, Long userId) {
        return likeRepository.existsByRecommendation_IdAndId_UserId(recommendationId, userId);
    }

    @Transactional(readOnly = true)
    public List<RecommendReadDto> searchRecommendations(String keyword, RecommendationType type) {
        List<Recommendation> recommendations = recommendationRepository.findAll().stream()
                .filter(rec -> type == null || rec.getType().equals(type))
                .filter(rec -> keyword == null || keyword.isEmpty() || 
                        rec.getTitle().contains(keyword) || 
                        rec.getSubTitle().contains(keyword) ||
                        rec.getTags().stream().anyMatch(tag -> tag.contains(keyword)))
                .toList();

        return recommendations.stream()
                .map(rec -> new RecommendReadDto(rec, false))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<RecommendReadDto> getRandomRecommendations(RecommendationType type, int limit) {
        List<Recommendation> allRecommendations = recommendationRepository.findAll();
        
        List<Recommendation> filteredRecommendations = new ArrayList<>(allRecommendations.stream()
                .filter(rec -> rec != null && rec.getType() != null)
                .filter(rec -> type == null || rec.getType().equals(type))
                .toList());
        
        if (filteredRecommendations.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 수동 셔플 (Collections.shuffle() 대신)
        for (int i = filteredRecommendations.size() - 1; i > 0; i--) {
            int j = (int) (Math.random() * (i + 1));
            Recommendation temp = filteredRecommendations.get(i);
            filteredRecommendations.set(i, filteredRecommendations.get(j));
            filteredRecommendations.set(j, temp);
        }
        
        return filteredRecommendations.stream()
                .limit(limit)
                .map(rec -> new RecommendReadDto(rec, false))
                .toList();
    }

    @Scheduled(fixedRate = 5000)
    @Transactional
    public void saveLike() {
        if (!likes.isEmpty()) {
            log.info("좋아요 저장 스케쥴링");
            likes.forEach((placeId, userActions) -> {
                Recommendation target = recommendationRepository.findById(placeId)
                        .orElseThrow(() -> {
                            log.warn(RecommendLogMessage.RECOMMEND_SEARCH_FAILED.format("추천 장소 조회 실패", placeId));
                            return new EntityNotFoundException("no place entity for like");
                        });

                userActions.forEach((userId, shouldLike) -> {
                    target.like(userId);
                });
            });
            likes.clear();
        }
    }
}
