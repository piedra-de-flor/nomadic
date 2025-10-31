package com.example.Triple_clone.domain.recommend.application;

import com.example.Triple_clone.common.file.FileManager;
import com.example.Triple_clone.common.logging.logMessage.MemberLogMessage;
import com.example.Triple_clone.common.logging.logMessage.RecommendLogMessage;
import com.example.Triple_clone.domain.member.domain.Member;
import com.example.Triple_clone.domain.member.infra.MemberRepository;
import com.example.Triple_clone.domain.recommend.domain.Recommendation;
import com.example.Triple_clone.domain.recommend.domain.RecommendationLikeId;
import com.example.Triple_clone.domain.recommend.domain.RecommendationType;
import com.example.Triple_clone.domain.recommend.infra.RecommendationLikeRepository;
import com.example.Triple_clone.domain.recommend.infra.RecommendationRepository;
import com.example.Triple_clone.domain.recommend.web.dto.RecommendReadDto;
import com.example.Triple_clone.domain.recommend.web.dto.RecommendReadTop10Dto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendQueryService {
    private final static int PAGE_SIZE = 5;

    private final RecommendationRepository recommendationRepository;
    private final RecommendationLikeRepository likeRepository;
    private final MemberRepository memberRepository;
    private final FileManager fileManager;
    private final RecommendLikeMap likes;


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

        recommendation.increaseViews();

        boolean likeOrNot;
        boolean dbLiked = likeRepository.existsById(new RecommendationLikeId(recommendationId, member.getId()));
        Boolean pending = likes.pendingState(member.getId(), recommendationId);
        likeOrNot = (pending != null) ? pending : dbLiked;

        return new RecommendReadDto(recommendation, member, likeOrNot);
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
        return recommendationRepository.findById(recommendationId)
                .orElseThrow(() -> new EntityNotFoundException("no place entity"));
    }

    @Transactional(readOnly = true)
    public Page<RecommendReadDto> findAll(String orderType, Pageable pageable, String email) {
        RecommendOrderType order = RecommendOrderType.valueOf(orderType);
        Pageable customPageable = PageRequest.of(
                pageable.getPageNumber(),
                PAGE_SIZE,
                Sort.by(order.getDirection(), order.getField())
        );

        Page<Recommendation> recPage = recommendationRepository.findAll(customPageable);

        Long userId = null;
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn(MemberLogMessage.MEMBER_SEARCH_FAILED_BY_EMAIL.format(email));
                    return new EntityNotFoundException("no user entity");
                });

        userId = member.getId();

        // 배치로 "DB 기준 좋아요" 식별
        Set<Long> likedIds = Set.of();
        List<Long> recIds = recPage.getContent().stream().map(Recommendation::getId).toList();
        if (!recIds.isEmpty()) {
            likedIds = likeRepository.findLikedRecIds(userId, recIds);
        }

        // 오버레이 반영: pendingState가 있으면 그 값을, 없으면 DB likedIds 사용
        Set<Long> finalLikedIds = likedIds;
        Long finalUserId = userId;

        List<RecommendReadDto> dtos = recPage.getContent().stream().map(rec -> {
            boolean dbLiked = finalLikedIds.contains(rec.getId());
            Boolean pending = likes.pendingState(finalUserId, rec.getId());
            boolean liked = (pending != null) ? pending : dbLiked;
            return new RecommendReadDto(rec, rec.getAuthor(), liked);
        }).toList();

        // 주의: pageable은 요청 pageable로 돌려주되, total은 원래 total 유지
        return new PageImpl<>(dtos, recPage.getPageable(), recPage.getTotalElements());
    }

    @Transactional(readOnly = true)
    public List<RecommendReadTop10Dto> findTop10() {
        List<Recommendation> top = recommendationRepository
                .findTop10ByTypeOrderByLikesCountDesc(RecommendationType.PLACE);

        return top.stream()
                .map(r -> new RecommendReadTop10Dto(r.getId(), r.getMainImage(), r.getTitle()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<RecommendReadDto> searchRecommendations(String keyword, RecommendationType type, String userEmail) {
        List<Recommendation> list = recommendationRepository.searchByKeywordAndType(keyword, type);

        Long userId = null;
        if (userEmail != null && !userEmail.isBlank()) {
            userId = memberRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new IllegalArgumentException("member not found"))
                    .getId();
        }

        Set<Long> likedIds = Set.of();
        if (userId != null && !list.isEmpty()) {
            likedIds = likeRepository.findLikedRecIds(userId, list.stream().map(Recommendation::getId).toList());
        }

        Long uid = userId;
        Set<Long> liked = likedIds;
        return list.stream().map(rec -> {
            boolean dbLiked = uid != null && liked.contains(rec.getId());
            Boolean pending = (uid != null) ? likes.pendingState(uid, rec.getId()) : null;
            boolean isLiked = (pending != null) ? pending : dbLiked;
            return new RecommendReadDto(rec, rec.getAuthor(), isLiked);
        }).toList();
    }

    @Transactional(readOnly = true)
    public List<RecommendReadDto> getRandomRecommendations(RecommendationType type, int limit, String userEmail) {
        // MySQL: ORDER BY RAND() LIMIT N (대규모면 다른 샘플링 전략 고려)
        List<Recommendation> list = recommendationRepository
                .findRandomByType(type != null ? type.name() : null, limit);

        Long userId = null;
        if (userEmail != null && !userEmail.isBlank()) {
            userId = memberRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new IllegalArgumentException("member not found"))
                    .getId();
        }

        Set<Long> likedIds = Set.of();
        if (userId != null && !list.isEmpty()) {
            likedIds = likeRepository.findLikedRecIds(userId, list.stream().map(Recommendation::getId).toList());
        }

        Long uid = userId;
        Set<Long> liked = likedIds;
        return list.stream().map(rec -> {
            boolean dbLiked = uid != null && liked.contains(rec.getId());
            Boolean pending = (uid != null) ? likes.pendingState(uid, rec.getId()) : null;
            boolean isLiked = (pending != null) ? pending : dbLiked;
            return new RecommendReadDto(rec, rec.getAuthor(), isLiked);
        }).toList();
    }
}
