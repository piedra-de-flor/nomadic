package com.example.Triple_clone.domain.entity;

import com.example.Triple_clone.domain.accommodation.Accommodation;
import com.example.Triple_clone.domain.vo.Location;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("R")
public class Reservation extends DetailPlan {
    @OneToOne
    private Accommodation accommodation;
    public Reservation(Plan plan, Location location, Date date, String time, Accommodation accommodation) {
        super(plan, location, date, time);
        this.accommodation = accommodation;
    }
}
