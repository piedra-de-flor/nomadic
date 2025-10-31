package com.example.Triple_clone.domain.recommend.application;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

@Getter
@Component
public class RecommendLikeMap {

    private final AtomicReference<ConcurrentHashMap<Long, ConcurrentHashMap<Long, Boolean>>> ref =
            new AtomicReference<>(new ConcurrentHashMap<>());

    public void put(long userId, long recId, boolean shouldLike) {
        ref.get().compute(recId, (rid, userMap) -> {
            if (userMap == null) userMap = new ConcurrentHashMap<>();
            userMap.put(userId, shouldLike);
            return userMap;
        });
    }

    public Map<Long, Map<Long, Boolean>> drain() {
        var old = ref.getAndSet(new ConcurrentHashMap<>());
        Map<Long, Map<Long, Boolean>> out = new HashMap<>();
        old.forEach((recId, users) -> out.put(recId, new HashMap<>(users)));
        return out;
    }

    public Boolean pendingState(long userId, long recId) {
        var m = ref.get().get(recId);
        return m != null ? m.get(userId) : null;
    }
}
