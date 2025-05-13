package com.example.Triple_clone.domain.entity;

import com.example.Triple_clone.domain.vo.ReportTargetType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReportCount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long targetId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportTargetType targetType;

    @Column(nullable = false)
    private Long count;

    public void incrementCount() {
        this.count++;
    }

    @Builder
    public ReportCount(Long targetId, ReportTargetType targetType, Long count) {
        this.targetId = targetId;
        this.targetType = targetType;
        this.count = count;
    }
}

