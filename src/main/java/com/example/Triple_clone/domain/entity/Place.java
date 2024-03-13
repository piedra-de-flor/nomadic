package com.example.Triple_clone.domain.entity;

import com.example.Triple_clone.domain.vo.Location;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("P")
public class Place extends DetailPlan{
    public Place(Plan plan, Location location, Date date, String time) {
        super(plan, location, date, time);
    }
}
