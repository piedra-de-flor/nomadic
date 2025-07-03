package com.example.Triple_clone.domain.recommend.domain;

import com.example.Triple_clone.domain.review.domain.Review;
import com.example.Triple_clone.common.file.Image;
import com.example.Triple_clone.domain.plan.domain.Location;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Recommendation {
    private static final long LIKE_INITIAL_NUMBER = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String title;
    private String notionUrl;
    private String subTitle;
    private Location location;
    private Image mainImage;
    private LocalDateTime date;

    @ElementCollection
    @CollectionTable(name = "recommendation_like", joinColumns = @JoinColumn(name = "recommendation_id"))
    private List<Long> likes = new ArrayList<>();

    @OneToMany(mappedBy = "recommendation")
    private List<Review> reviews = new ArrayList<>();

    @Builder
    public Recommendation(@NonNull String title, String notionUrl, String subTitle, Location location) {
        this.title = title;
        this.notionUrl = notionUrl;
        this.subTitle = subTitle;
        this.location = location;
        this.date = LocalDateTime.now();
    }

    public void update(String title, String notionUrl, String subTitle, Location location) {
        if (title.isEmpty()) {
            throw new IllegalArgumentException("there is no title");
        }
        this.title = title;
        this.notionUrl = notionUrl;
        this.subTitle = subTitle;
        this.location = location;
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

    public String setImage(Image image) {
        this.mainImage = image;
        return image.getStoredFileName();
    }
}
