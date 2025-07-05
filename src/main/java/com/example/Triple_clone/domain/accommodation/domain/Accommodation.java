package com.example.Triple_clone.domain.accommodation.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    public Accommodation(String local, String name, double score, String category, long lentDiscountRate, int lentTime, long lentOriginPrice, long lentPrice, boolean lentStatus, String enterTime, long lodgmentDiscountRate, long lodgmentOriginPrice, long lodgmentPrice, boolean lodgmentStatus) {
        this.local = local;
        this.name = name;
        this.score = score;
        this.category = category;
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
