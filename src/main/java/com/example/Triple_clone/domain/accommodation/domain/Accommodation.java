package com.example.Triple_clone.domain.accommodation.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
@Entity
@Table(name = "accommodations")
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
    private Boolean hasDayuseDiscount;
    private Integer dayusePrice;
    private Integer dayuseSalePrice;
    private Boolean dayuseSoldout;
    @Column(length = 50)
    private String dayuseTime;
    private Boolean hasStayDiscount;
    private Integer stayPrice;
    private Integer staySalePrice;
    private Boolean staySoldout;
    @Column(length = 50)
    private String stayCheckinTime;
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
            Boolean hasDayuseDiscount,
            Integer dayusePrice,
            Integer dayuseSalePrice,
            Boolean dayuseSoldout,
            String dayuseTime,
            Boolean hasStayDiscount,
            Integer stayPrice,
            Integer staySalePrice,
            Boolean staySoldout,
            String stayCheckinTime,
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
        this.hasDayuseDiscount = hasDayuseDiscount;
        this.dayusePrice = dayusePrice;
        this.dayuseSalePrice = dayuseSalePrice;
        this.dayuseSoldout = dayuseSoldout;
        this.dayuseTime = dayuseTime;
        this.hasStayDiscount = hasStayDiscount;
        this.stayPrice = stayPrice;
        this.staySalePrice = staySalePrice;
        this.staySoldout = staySoldout;
        this.stayCheckinTime = stayCheckinTime;
        this.intro = intro;
        this.amenities = amenities;
        this.info = info;
    }
}
