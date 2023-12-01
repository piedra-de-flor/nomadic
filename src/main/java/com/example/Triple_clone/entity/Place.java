package com.example.Triple_clone.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "place")
@Entity
public class Place {
    private static final long LIKE_INITIAL_NUMBER = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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
    public Place(String title, String notionUrl, String subTitle, String location, String mainImage) {
        this.title = title;
        this.notionUrl = notionUrl;
        this.subTitle = subTitle;
        this.location = location;
        this.mainImage = mainImage;
        this.date = LocalDateTime.now();
        this.likes = new ArrayList<>();
        this.reviews = new ArrayList<>();
    }

    public void update(Optional<String> title, Optional<String> notionUrl, Optional<String> subTitle, Optional<String> location, Optional<String> mainImage) {
        title.ifPresent(update -> this.title = update);
        notionUrl.ifPresent(update -> this.notionUrl = update);
        subTitle.ifPresent(update -> this.subTitle = update);
        location.ifPresent(update -> this.location = update);
        mainImage.ifPresent(update -> this.mainImage = update);
        this.date = LocalDateTime.now();
    }

    public int getLikesNumber() {
        return likes.size();
    }

    public int getReviewsNumber() {
        return reviews.size();
    }
}
