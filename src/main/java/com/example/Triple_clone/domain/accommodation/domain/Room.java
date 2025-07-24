package com.example.Triple_clone.domain.accommodation.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "rooms")
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
    @Column(length = 50)
    private String dayuseTime;
    @Column(length = 50)
    private String stayCheckinTime;
    @Column(length = 50)
    private String stayCheckoutTime;
    private Integer stayPrice;
    private Integer capacity;
    private Integer maxCapacity;
}
