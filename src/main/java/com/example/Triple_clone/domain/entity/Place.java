package com.example.Triple_clone.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "place")
@Entity
public class Place {
    private static final long LIKE_INITIAL_NUMBER = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String title;
    private String notionUrl;
    private String subTitle;
    private String location;
    private String mainImage;
    private LocalDateTime date;

    @ElementCollection
    @CollectionTable(name = "place_like", joinColumns = @JoinColumn(name = "place_id"))
    private List<Long> likes;

    @OneToMany(mappedBy = "place")
    private List<Review> reviews;

    @Builder
    public Place(@NonNull String title, String notionUrl, String subTitle, String location, String mainImage) {
        this.title = title;
        this.notionUrl = notionUrl;
        this.subTitle = subTitle;
        this.location = location;
        this.mainImage = mainImage;
        this.date = LocalDateTime.now();
        this.likes = new ArrayList<>();
        this.reviews = new ArrayList<>();
    }

    public void update(String title, String notionUrl, String subTitle, String location, String mainImage) {
        if (title.isEmpty()) {
            throw new IllegalArgumentException("there is no title");
        }
        this.title = title;
        this.notionUrl = notionUrl;
        this.subTitle = subTitle;
        this.location = location;
        this.mainImage = mainImage;
        this.date = LocalDateTime.now();
    }

    public void like(long userId) {
        if (isLikedBy(userId)) {
            likes.remove(userId);
            return;
        }
        likes.add(userId);
    }

    public boolean isLikedBy(long userId) {
        return likes.contains(userId);
    }

    public void addReview(Review review) {
        reviews.add(review);
    }

    public int getLikesNumber() {
        return likes.size();
    }

    public int getReviewsNumber() {
        return reviews.size();
    }
}
