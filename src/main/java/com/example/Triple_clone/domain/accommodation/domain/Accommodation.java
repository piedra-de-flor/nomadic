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
        if (dto.hasDayuseDiscount() != null) this.hasDayuseDiscount = dto.hasDayuseDiscount();
        if (dto.dayusePrice() != null) this.dayusePrice = dto.dayusePrice();
        if (dto.dayuseSalePrice() != null) this.dayuseSalePrice = dto.dayuseSalePrice();
        if (dto.dayuseSoldout() != null) this.dayuseSoldout = dto.dayuseSoldout();
        if (dto.dayuseTime() != null) this.dayuseTime = dto.dayuseTime();
        if (dto.hasStayDiscount() != null) this.hasStayDiscount = dto.hasStayDiscount();
        if (dto.stayPrice() != null) this.stayPrice = dto.stayPrice();
        if (dto.staySalePrice() != null) this.staySalePrice = dto.staySalePrice();
        if (dto.staySoldout() != null) this.staySoldout = dto.staySoldout();
        if (dto.stayCheckinTime() != null) this.stayCheckinTime = dto.stayCheckinTime();
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
