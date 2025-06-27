package com.example.Triple_clone.domain.entity;

import com.example.Triple_clone.domain.member.Member;
import com.example.Triple_clone.domain.vo.ReportStatus;
import com.example.Triple_clone.domain.vo.ReportTargetType;
import com.example.Triple_clone.domain.vo.ReportingReason;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id", nullable = false)
    private Member reporter;

    @Column(nullable = false)
    private Long targetId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportTargetType targetType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportingReason reason;

    private String detail;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportStatus status = ReportStatus.PENDING;

    @Column
    private LocalDateTime processedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @Builder
    public Report(Member reporter, Long targetId, ReportTargetType targetType, ReportingReason reason, String detail) {
        this.reporter = reporter;
        this.targetId = targetId;
        this.targetType = targetType;
        this.reason = reason;
        this.detail = detail;
    }

    public static Report of(Reportable target, Member reporter, ReportingReason reason, String detail) {
        return Report.builder()
                .reporter(reporter)
                .targetId(target.getId())
                .targetType(target.getReportType())
                .reason(reason)
                .detail(detail)
                .build();
    }

    public void approve() {
        this.status = ReportStatus.APPROVED;
        this.processedAt = LocalDateTime.now();
    }

    public void reject() {
        this.status = ReportStatus.REJECTED;
        this.processedAt = LocalDateTime.now();
    }
}
