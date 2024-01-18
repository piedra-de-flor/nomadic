package com.example.Triple_clone.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "place_id")
    private Place place;

    private String content;
    private String image;

    public Review(User user, Place place, String content, String image) {
        this.user = user;
        this.place = place;
        this.content = content;
        this.image = image;
    }
}
