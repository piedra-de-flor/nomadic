package com.example.Triple_clone.domain.entity;

import com.example.Triple_clone.domain.vo.Image;
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
    private Member member;

    @ManyToOne
    @JoinColumn(name = "recommendation_id")
    private Recommendation recommendation;

    private String content;
    private Image image;

    public Review(Member member, Recommendation recommendation, String content) {
        this.member = member;
        this.recommendation = recommendation;
        this.content = content;
    }

    public String setImage(Image image) {
        this.image = image;
        return image.getStoredFileName();
    }
}
