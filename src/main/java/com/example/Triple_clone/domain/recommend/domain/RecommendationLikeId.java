package com.example.Triple_clone.domain.recommend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationLikeId implements Serializable {
    @Column(name = "recommendation_id")
    private Long recommendationId;

    @Column(name = "user_id")
    private Long userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecommendationLikeId)) return false;
        RecommendationLikeId that = (RecommendationLikeId) o;
        return Objects.equals(recommendationId, that.recommendationId)
                && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recommendationId, userId);
    }
}
