package com.example.Triple_clone.domain.recommend.domain;

import com.example.Triple_clone.common.error.AuthErrorCode;
import com.example.Triple_clone.common.error.RestApiException;
import com.example.Triple_clone.common.file.Image;
import com.example.Triple_clone.common.logging.logMessage.RecommendLogMessage;
import com.example.Triple_clone.domain.member.domain.Member;
import com.example.Triple_clone.domain.plan.domain.Location;
import com.example.Triple_clone.domain.review.domain.Review;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "recommendation")
public class Recommendation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String subTitle;
    @Embedded
    private Location location;
    @Embedded
    private Image mainImage;
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String price;

    @ElementCollection
    @CollectionTable(name = "recommendation_tag",
            joinColumns = @JoinColumn(name = "recommendation_id"))
    @Column(name = "tag")
    private Set<String> tags = new LinkedHashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecommendationType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Member author;

    @OneToMany(mappedBy = "recommendation",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @OrderBy("orderIndex ASC")
    @JsonManagedReference
    private List<RecommendationBlock> blocks = new ArrayList<>();

    @OneToMany(mappedBy = "recommendation",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    private int likesCount;
    private int reviewsCount;
    private int viewsCount;

    public void addBlock(RecommendationBlock b) {
        b.setRecommendation(this);
        blocks.add(b);
    }
    public void removeBlock(RecommendationBlock b) {
        blocks.remove(b);
        b.setRecommendation(null);
    }


    @Builder
    public Recommendation(@NonNull String title, String subTitle, Location location, 
                         String price, RecommendationType type, Member author,
                         List<RecommendationBlock> blocks) {
        this.title = title;
        this.subTitle = subTitle;
        this.location = location;
        this.price = price;
        this.type = type;
        this.author = author;
        this.blocks = blocks != null ? blocks : new ArrayList<>();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.likesCount = 0;
        this.reviewsCount = 0;
        this.viewsCount = 0;
    }

    public void update(String title, String subTitle, Location location, String price) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("제목은 필수입니다");
        }
        this.title = title;
        this.subTitle = subTitle;
        this.location = location;
        this.price = price;
        this.updatedAt = LocalDateTime.now();
    }

    public void addReview(Review review) {
        reviews.add(review);
        reviewsCount++;
    }
    
    public void removeReview(Review review) {
        reviews.remove(review);
        reviewsCount = Math.max(0, reviewsCount - 1);
    }

    public String setImage(Image image) {
        this.mainImage = image;
        return image.getStoredFileName();
    }
    
    public void increaseViews() {
        this.viewsCount++;
    }
    
    public void clearTags() {
        this.tags.clear();
    }
    
    public void addTags(Set<String> newTags) {
        if (newTags != null) {
            newTags.stream()
                    .filter(tag -> tag != null && !tag.trim().isEmpty())
                    .forEach(tag -> this.tags.add(tag.trim()));
        }
    }
    
    public void updateTags(Set<String> newTags) {
        clearTags();
        addTags(newTags);
    }

    public boolean isMine(Member member) {
        if (member.getId() == author.getId()) {
            return true;
        }

        log.warn(RecommendLogMessage.RECOMMEND_AUTH_FAILED.format(author.getId()));
        throw new RestApiException(AuthErrorCode.AUTH_ERROR_CODE);
    }
}
