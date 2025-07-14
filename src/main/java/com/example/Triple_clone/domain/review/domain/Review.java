package com.example.Triple_clone.domain.review.domain;

import com.example.Triple_clone.domain.recommend.domain.Recommendation;
import com.example.Triple_clone.domain.member.domain.Member;
import com.example.Triple_clone.domain.report.domain.Reportable;
import com.example.Triple_clone.common.file.Image;
import com.example.Triple_clone.domain.report.domain.ReportTargetType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Review implements Reportable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "recommendation_id")
    private Recommendation recommendation;

    private String content;
    private Image image;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Review parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> children = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private ReviewStatus status = ReviewStatus.ACTIVE;

    public Review(Member member, Recommendation recommendation, String content) {
        this.member = member;
        this.recommendation = recommendation;
        this.content = content;
    }

    public String setImage(Image image) {
        this.image = image;
        return image.getStoredFileName();
    }

    public void update(String content) {
        this.content = content;
    }

    public void addChildReview(Review child) {
        children.add(child);
        child.parent = this;
    }

    public String getWriter() {
        return member.getName();
    }

    public void softDelete() {
        this.status = ReviewStatus.DELETED;
    }

    public boolean isDeleted() {
        return this.status == ReviewStatus.DELETED;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public ReportTargetType getReportType() {
        return ReportTargetType.REVIEW;
    }
}
