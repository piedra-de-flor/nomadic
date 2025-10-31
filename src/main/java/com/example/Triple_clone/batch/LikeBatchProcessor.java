package com.example.Triple_clone.batch;

import com.example.Triple_clone.domain.recommend.application.RecommendLikeMap;
import com.example.Triple_clone.domain.recommend.infra.RecommendationLikeRepository;
import com.example.Triple_clone.domain.recommend.infra.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class LikeBatchProcessor {
    private static final int CHUNK = 1000;
    final String INSERT_SQL = "INSERT IGNORE INTO recommendation_like (recommendation_id, user_id) VALUES (?, ?)";

    private final RecommendLikeMap likesMap;
    private final RecommendationLikeRepository likeRepository;
    private final RecommendationRepository recommendationRepository;
    private final JdbcTemplate jdbcTemplate;

    @Scheduled(fixedDelayString = "${like.flush.delay-ms:3000}")
    @Transactional
    public void flush() {
        Map<Long, Map<Long, Boolean>> snapshot = likesMap.drain();
        if (snapshot.isEmpty()) return;

        log.info("LikeBatchProcessor 시작: {}개 rec", snapshot.size());

        snapshot.forEach((recId, actions) -> {
            if (actions.isEmpty()) return;
            if (!recommendationRepository.existsById(recId)) {
                log.warn("recommendation {} 없음 → 스킵", recId);
                return;
            }

            List<Long> users = new ArrayList<>(actions.keySet());
            for (int i = 0; i < users.size(); i += CHUNK) {
                List<Long> slice = users.subList(i, Math.min(i + CHUNK, users.size()));
                Set<Long> existing = new HashSet<>(likeRepository.findExistingUserIds(recId, slice));
                List<Long> toLike = new ArrayList<>();
                List<Long> toUnlike = new ArrayList<>();

                for (Long uid : slice) {
                    boolean wantLike = Boolean.TRUE.equals(actions.get(uid));
                    if (wantLike) {
                        if (!existing.contains(uid)) toLike.add(uid);
                    } else {
                        if (existing.contains(uid)) toUnlike.add(uid);
                    }
                }

                int deleted = 0;
                if (!toUnlike.isEmpty()) {
                    deleted = likeRepository.bulkDelete(recId, toUnlike);
                    if (deleted > 0) recommendationRepository.incrementLikeCount(recId, -deleted);
                }

                int inserted = 0;
                if (!toLike.isEmpty()) {
                    inserted = bulkInsertIgnore(recId, toLike);
                    if (inserted > 0) recommendationRepository.incrementLikeCount(recId, +inserted);
                }

                if (log.isDebugEnabled()) {
                    log.debug("recId={} chunk={} inserted={} deleted={} delta={}",
                            recId, slice.size(), inserted, deleted, inserted - deleted);
                }
            }
        });

        log.info("LikeBatchProcessor 완료");
    }

    private int bulkInsertIgnore(Long recId, List<Long> userIds) {
        List<Object[]> args = userIds.stream().map(uid -> new Object[]{recId, uid}).toList();
        int[] counts = jdbcTemplate.batchUpdate(INSERT_SQL, args);
        int success = 0;
        for (int c : counts) {
            if (c > 0) success++;
        }
        return success;
    }
}
