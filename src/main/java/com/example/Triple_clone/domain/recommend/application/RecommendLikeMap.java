package com.example.Triple_clone.domain.recommend.application;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Getter
@Component
public class RecommendLikeMap {
    ConcurrentHashMap<Long, ConcurrentHashMap<Long, Boolean>> likes = new ConcurrentHashMap<>();

    public void insert(long memberId, long recommendationId) {
        likes.compute(recommendationId, (rid, userMap) -> {
            if (userMap == null) userMap = new ConcurrentHashMap<>();
            userMap.compute(memberId, (uid, prev) -> (prev == null) ? Boolean.TRUE : null);
            return userMap.isEmpty() ? null : userMap;
        });
    }
}
