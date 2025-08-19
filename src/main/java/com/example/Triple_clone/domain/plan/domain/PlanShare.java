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
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"plan_id", "member_id"})
})
public class PlanShare {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShareRole role;

    @Column(nullable = false)
    private LocalDateTime sharedAt;

    private LocalDateTime acceptedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShareStatus status;

    @Builder
    public PlanShare(Plan plan, Member member, ShareRole role) {
        this.plan = plan;
        this.member = member;
        this.role = role;
        this.sharedAt = LocalDateTime.now();
        this.status = ShareStatus.PENDING;
    }

    public void accept() {
        this.status = ShareStatus.ACCEPTED;
        this.acceptedAt = LocalDateTime.now();
    }

    public void reject() {
        this.status = ShareStatus.REJECTED;
    }

    public boolean canEdit() {
        return this.status == ShareStatus.ACCEPTED && this.role == ShareRole.EDITOR;
    }

    public boolean canView() {
        return this.status == ShareStatus.ACCEPTED && this.role != null;
    }

    public boolean isSharedWith(Member member) {
        return this.member.getId() == member.getId();
    }
}