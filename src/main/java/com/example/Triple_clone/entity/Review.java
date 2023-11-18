package com.example.Triple_clone.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private long reviewId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "place_id")
    private Place place;

    @Column(name = "content")
    private String content;
    private String image;

    public Review(User user, Place place, String content, String image) {
        this.user = user;
        this.place = place;
        this.content = content;
        this.image = image;
    }
}
