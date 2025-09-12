package com.example.Triple_clone.domain.accommodation.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accommodation_id")
    private Accommodation accommodation;

    @Column(length = 255)
    private String name;
    private Integer dayusePrice;
    private Integer dayuseSalePrice;
    private Boolean hasDayuseDiscount;
    private Boolean dayuseSoldout;

    @Column(length = 50)
    private String dayuseTime;

    private Integer stayPrice;
    private Integer staySalePrice;
    private Boolean hasStayDiscount;
    private Boolean staySoldout;
    @Column(length = 50)
    private String stayCheckinTime;
    @Column(length = 50)
    private String stayCheckoutTime;
    private Integer capacity;
    private Integer maxCapacity;

    @Builder
    public Room(
            Accommodation accommodation,
            String name,
            Integer dayusePrice,
            Integer dayuseSalePrice,
            Boolean hasDayuseDiscount,
            Boolean dayuseSoldout,
            String dayuseTime,
            Integer stayPrice,
            Integer staySalePrice,
            Boolean hasStayDiscount,
            Boolean staySoldout,
            String stayCheckinTime,
            String stayCheckoutTime,
            Integer capacity,
            Integer maxCapacity
    ) {
        this.accommodation = accommodation;
        this.name = name;
        this.dayusePrice = dayusePrice;
        this.dayuseSalePrice = dayuseSalePrice;
        this.hasDayuseDiscount = hasDayuseDiscount;
        this.dayuseSoldout = dayuseSoldout;
        this.dayuseTime = dayuseTime;
        this.stayPrice = stayPrice;
        this.staySalePrice = staySalePrice;
        this.hasStayDiscount = hasStayDiscount;
        this.staySoldout = staySoldout;
        this.stayCheckinTime = stayCheckinTime;
        this.stayCheckoutTime = stayCheckoutTime;
        this.capacity = capacity;
        this.maxCapacity = maxCapacity;
    }

    public void update(
            String name,
            Integer dayusePrice,
            Integer dayuseSalePrice,
            Boolean hasDayuseDiscount,
            Boolean dayuseSoldout,
            String dayuseTime,
            Integer stayPrice,
            Integer staySalePrice,
            Boolean hasStayDiscount,
            Boolean staySoldout,
            String stayCheckinTime,
            String stayCheckoutTime,
            Integer capacity,
            Integer maxCapacity
    ) {
        if (name != null) this.name = name;
        if (dayusePrice != null) this.dayusePrice = dayusePrice;
        if (dayuseSalePrice != null) this.dayuseSalePrice = dayuseSalePrice;
        if (hasDayuseDiscount != null) this.hasDayuseDiscount = hasDayuseDiscount;
        if (dayuseSoldout != null) this.dayuseSoldout = dayuseSoldout;
        if (dayuseTime != null) this.dayuseTime = dayuseTime;
        if (stayPrice != null) this.stayPrice = stayPrice;
        if (staySalePrice != null) this.staySalePrice = staySalePrice;
        if (hasStayDiscount != null) this.hasStayDiscount = hasStayDiscount;
        if (staySoldout != null) this.staySoldout = staySoldout;
        if (stayCheckinTime != null) this.stayCheckinTime = stayCheckinTime;
        if (stayCheckoutTime != null) this.stayCheckoutTime = stayCheckoutTime;
        if (capacity != null) this.capacity = capacity;
        if (maxCapacity != null) this.maxCapacity = maxCapacity;
    }

    public void setAccommodation(Accommodation accommodation) {
        this.accommodation = accommodation;
    }
}