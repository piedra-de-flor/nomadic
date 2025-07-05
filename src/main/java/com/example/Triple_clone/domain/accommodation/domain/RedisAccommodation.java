package com.example.Triple_clone.domain.accommodation.domain;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.time.LocalTime;

@Getter
@AllArgsConstructor
@RedisHash(value="accommodation")
public class RedisAccommodation {
    @Id
    private long id;
    @Indexed
    private String local;
    private String name;
    private double score;
    private String category;
    private int lentTime;
    private long lentPrice;
    private boolean lentStatus;
    private LocalTime enterTime;
    private long discountRate;
    private long originPrice;
    private long totalPrice;
}
