package com.example.Triple_clone.domain.accommodation.domain;

import com.example.Triple_clone.domain.accommodation.web.dto.AccommodationDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@RequiredArgsConstructor
@Entity
public class Accommodation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(length = 500)
    private String image;
    @Column(length = 255)
    private String name;
    @Column(length = 100)
    private String category;
    @Column(length = 50)
    private String grade;
    private Float rating;
    private Integer reviewCount;
    @Column(length = 100)
    private String region;
    @Column(length = 500)
    private String address;
    @Column(length = 200)
    private String landmarkDistance;
    @Lob
    private String intro;
    @Lob
    private String amenities;
    @Lob
    private String info;

    @OneToMany(mappedBy = "accommodation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Room> rooms = new ArrayList<>();


    @Builder
    public Accommodation(
            String image,
            String name,
            String category,
            String grade,
            Float rating,
            Integer reviewCount,
            String region,
            String address,
            String landmarkDistance,
            String intro,
            String amenities,
            String info
    ) {
        this.image = image;
        this.name = name;
        this.category = category;
        this.grade = grade;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.region = region;
        this.address = address;
        this.landmarkDistance = landmarkDistance;
        this.intro = intro;
        this.amenities = amenities;
        this.info = info;
    }

    public void update(AccommodationDto dto) {
        if (dto.image() != null) this.image = dto.image();
        if (dto.name() != null) this.name = dto.name();
        if (dto.category() != null) this.category = dto.category();
        if (dto.grade() != null) this.grade = dto.grade();
        if (dto.rating() != null) this.rating = dto.rating();
        if (dto.reviewCount() != null) this.reviewCount = dto.reviewCount();
        if (dto.region() != null) this.region = dto.region();
        if (dto.address() != null) this.address = dto.address();
        if (dto.landmarkDistance() != null) this.landmarkDistance = dto.landmarkDistance();
        if (dto.intro() != null) this.intro = dto.intro();
        if (dto.amenities() != null) this.amenities = dto.amenities();
        if (dto.info() != null) this.info = dto.info();
    }

    public void addRoom(Room room) {
        boolean exists = rooms.stream()
                .anyMatch(r -> Objects.equals(r.getName(), room.getName()));
        if (!exists) {
            rooms.add(room);
        }
    }
}
