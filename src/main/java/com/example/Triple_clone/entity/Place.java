package com.example.Triple_clone.entity;

import com.example.Triple_clone.vo.entity.Photo;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
@Table(name = "place")
@Entity
public class Place {
    private static final long LIKE_INITIAL_NUMBER = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "notion_url")
    private String notionUrl;

    @Column(name = "sub_title")
    private String subTitle;

    @Column(name = "location")
    private String location;

    @Column(name = "main_photo")
    @Embedded
    private Photo mainPhoto;

    @Column(name = "regist_date")
    private String date;

    @Column(name = "likes")
    @ElementCollection
    private List<Long> likes;

    @Builder
    public Place(String title, String notionUrl, String subTitle, String location, String mainPhoto) {
        Place.builder()
                .title(title)
                .notionUrl(notionUrl)
                .subTitle(subTitle)
                .location(location)
                .mainPhoto(mainPhoto)
                .build();

        this.date = LocalDateTime.now().toString();
        this.likes = new ArrayList<>();
    }

    public int getLikesNumber() {
        return likes.size();
    }
}
