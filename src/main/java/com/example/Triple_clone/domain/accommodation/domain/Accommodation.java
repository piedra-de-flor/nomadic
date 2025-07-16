package com.example.Triple_clone.domain.accommodation.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Getter
@RequiredArgsConstructor
@Entity
public class Accommodation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String local;
    private String name;
    private double score;
    private String category;
    private String imageUrl;

    @Lob
    private String description;

    @Lob
    private String detailDescription;

    @Lob
    private String services;
    private long lentDiscountRate;
    private int lentTime;
    private long lentOriginPrice;
    private long lentPrice;
    private boolean lentStatus;
    private LocalTime enterTime;
    private long lodgmentDiscountRate;
    private long lodgmentOriginPrice;
    private long lodgmentPrice;
    private boolean lodgmentStatus;

    @Builder
    public Accommodation(String local, String name, double score, String category, long lentDiscountRate, int lentTime,
                         long lentOriginPrice, long lentPrice, boolean lentStatus, String enterTime, long lodgmentDiscountRate,
                         long lodgmentOriginPrice, long lodgmentPrice, boolean lodgmentStatus, String imageUrl, String description,
                         String detailDescription, String services) {
        this.local = local;
        this.name = name;
        this.score = score;
        this.category = category;
        this.imageUrl = imageUrl;
        this.description = description;
        this.detailDescription = detailDescription;
        this.services = services;
        this.lentDiscountRate = lentDiscountRate;
        this.lentTime = lentTime;
        this.lentOriginPrice = lentOriginPrice;
        this.lentPrice = lentPrice;
        this.lentStatus = lentStatus;
        this.enterTime = null;
        if (enterTime != null) {
            this.enterTime = LocalTime.parse(enterTime, DateTimeFormatter.ofPattern("HH:mm"));
        }
        this.lodgmentDiscountRate = lodgmentDiscountRate;
        this.lodgmentOriginPrice = lodgmentOriginPrice;
        this.lodgmentPrice = lodgmentPrice;
        this.lodgmentStatus = lodgmentStatus;
    }
}
