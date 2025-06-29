package com.example.Triple_clone.domain.plan.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Embeddable
public class Location {
    private Double latitude;
    private Double longitude;
    private String name;
}
