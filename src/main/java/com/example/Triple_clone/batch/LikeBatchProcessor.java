package com.example.Triple_clone.batch;

import com.example.Triple_clone.common.logging.logMessage.RecommendLogMessage;
import com.example.Triple_clone.domain.recommend.application.RecommendLikeMap;
import com.example.Triple_clone.domain.recommend.domain.Recommendation;
import com.example.Triple_clone.domain.recommend.infra.RecommendationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class LikeBatchProcessor {
    private final RecommendationRepository recommendationRepository;
    private final RecommendLikeMap likesMap;

    @Scheduled(fixedRate = 5000)
    @Transactional
    public void saveLike() {
        ConcurrentHashMap<Long, ConcurrentHashMap<Long, Boolean>> likes = likesMap.getLikes();

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
