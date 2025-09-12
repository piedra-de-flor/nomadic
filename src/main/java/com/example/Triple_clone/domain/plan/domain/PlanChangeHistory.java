package com.example.Triple_clone.domain.plan.domain;

import com.example.Triple_clone.domain.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class PlanChangeHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member changedBy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChangeType changeType;
    private Long targetId;
    private String targetType;

    @Lob
    @Column(name = "change_data", columnDefinition = "TEXT")
    private String changeData;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public PlanChangeHistory(Plan plan, Member changedBy, ChangeType changeType,
                             Long targetId, String targetType, String changeData) {
        this.plan = plan;
        this.changedBy = changedBy;
        this.changeType = changeType;
        this.targetId = targetId;
        this.targetType = targetType;
        this.changeData = changeData;
        this.createdAt = LocalDateTime.now();
    }
}