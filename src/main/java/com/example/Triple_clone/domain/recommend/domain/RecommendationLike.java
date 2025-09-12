package com.example.Triple_clone.domain.recommend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "recommendation_like")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationLike {

    @EmbeddedId
    private RecommendationLikeId id;

    @MapsId("recommendationId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recommendation_id", nullable = false)
    private Recommendation recommendation;

    private LocalDateTime createdAt;

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecommendationLike)) return false;
        return Objects.equals(id, ((RecommendationLike)o).id);
    }
    @Override public int hashCode() { return Objects.hash(id); }
}