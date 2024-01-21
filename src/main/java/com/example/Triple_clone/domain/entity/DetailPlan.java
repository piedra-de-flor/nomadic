package com.example.Triple_clone.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.util.Date;

@Entity
public class DetailPlan {
    @Id
    private long id;
    @ManyToOne
    @JoinColumn(name = "plan_id")
    private Plan plan;
    private String name;
    private String location;
    private Date date;
    private String time;
}
