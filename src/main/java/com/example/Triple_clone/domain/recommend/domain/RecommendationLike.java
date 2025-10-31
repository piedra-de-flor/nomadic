package com.example.Triple_clone.domain.recommend.domain;

import com.example.Triple_clone.domain.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
public class RecommendationLike {
    @EmbeddedId
    private RecommendationLikeId id;

    @MapsId("recommendationId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recommendation_id")
    private Recommendation recommendation;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private Member user;

    @Column(name = "created_at", nullable = false, updatable = false, insertable = false,
            columnDefinition = "datetime(3) default current_timestamp(3)")
    private LocalDateTime createdAt;

    private RecommendationLike(Recommendation rec, Member user) {
        this.recommendation = rec;
        this.user = user;
        this.id = new RecommendationLikeId(rec.getId(), user.getId());
    }
    public static RecommendationLike of(Recommendation rec, Member user) { return new RecommendationLike(rec, user); }
}