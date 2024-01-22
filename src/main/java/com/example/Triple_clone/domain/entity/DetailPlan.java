package com.example.Triple_clone.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class DetailPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "plan_id")
    private Plan plan;
    private String name;
    private String location;
    private Date date;
    private String time;

    @Builder
    public DetailPlan(Plan plan, String name, String location, Date date, String time) {
        DetailPlan.builder()
                .plan(plan)
                .name(name)
                .location(location)
                .date(date)
                .time(time)
                .build();
    }
}
