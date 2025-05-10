package com.example.Triple_clone.domain.entity;

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

    @Column(nullable = false)
    private String targetType;

    @Column(nullable = false)
    private Long count;

    public void incrementCount() {
        this.count++;
    }

    @Builder
    public ReportCount(Long targetId, String targetType, Long count) {
        this.targetId = targetId;
        this.targetType = targetType;
        this.count = count;
    }
}

