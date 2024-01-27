package com.example.Triple_clone.domain.entity;

import com.example.Triple_clone.domain.vo.Location;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class DetailPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "plan_id")
    private Plan plan;
    @Embedded
    private Location location;
    private Date date;
    private String time;

    @Builder
    public DetailPlan(Plan plan, Location location, Date date, String time) {
        this.plan = plan;
        this.location = location;
        this.date = date;
        this.time = time;
    }

    public void update(Location location, Date date, String time) {
        this.location = location;
        this.date = date;
        this.time = time;
    }
}
