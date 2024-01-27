package com.example.Triple_clone.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@Getter
@Embeddable
public class Location {
    private Double latitude;
    private Double longitude;
    private String name;

    public Location() {

    }
}

