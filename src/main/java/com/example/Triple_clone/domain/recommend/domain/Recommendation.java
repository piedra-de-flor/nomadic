package com.example.Triple_clone.domain.recommend.domain;

import com.example.Triple_clone.domain.review.domain.Review;
import com.example.Triple_clone.common.file.Image;
import com.example.Triple_clone.domain.plan.domain.Location;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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

    @Embedded
    private PostMeta postMeta;

    @OneToMany(mappedBy = "recommendation",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @OrderBy("orderIndex ASC")
    @JsonManagedReference
    private List<RecommendationBlock> blocks = new ArrayList<>();

    // 좋아요/리뷰 (정규화)
    @OneToMany(mappedBy = "recommendation",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Set<RecommendationLike> likes = new LinkedHashSet<>();

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
                         String price, RecommendationType type, PostMeta postMeta,
                         List<RecommendationBlock> blocks) {
        this.title = title;
        this.subTitle = subTitle;
        this.location = location;
        this.price = price;
        this.type = type;
        this.postMeta = postMeta;
        this.blocks = blocks != null ? blocks : new ArrayList<>();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.likesCount = 0;
        this.reviewsCount = 0;
        this.viewsCount = 0;
    }

    public void update(String title, String subTitle, Location location, String price, PostMeta postMeta) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("제목은 필수입니다");
        }
        this.title = title;
        this.subTitle = subTitle;
        this.location = location;
        this.price = price;
        this.postMeta = postMeta;
        this.updatedAt = LocalDateTime.now();
    }

    public void like(long userId) {
        if (this.id == null) {
            throw new IllegalStateException("Recommendation must be saved before adding likes");
        }
        
        RecommendationLike existingLike = findLikeByUserId(userId);
        if (existingLike != null) {
            likes.remove(existingLike);
            decreaseLikesCount();
        } else {
            RecommendationLikeId likeId = new RecommendationLikeId(this.id, userId);
            RecommendationLike newLike = new RecommendationLike();
            newLike.setId(likeId);
            newLike.setRecommendation(this);
            newLike.setCreatedAt(LocalDateTime.now());
            
            likes.add(newLike);
            increaseLikesCount();
        }
    }

    public boolean isLikedBy(long userId) {
        return findLikeByUserId(userId) != null;
    }
    
    private RecommendationLike findLikeByUserId(long userId) {
        return likes.stream()
                .filter(like -> like.getId().getUserId().equals(userId))
                .findFirst()
                .orElse(null);
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
    
    public void addTag(String tag) {
        if (tag != null && !tag.trim().isEmpty()) {
            this.tags.add(tag.trim());
        }
    }
    
    public void removeTag(String tag) {
        this.tags.remove(tag);
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
    
    public void decreaseLikesCount() {
        this.likesCount = Math.max(0, this.likesCount - 1);
    }
    
    public void increaseLikesCount() {
        this.likesCount++;
    }
}
