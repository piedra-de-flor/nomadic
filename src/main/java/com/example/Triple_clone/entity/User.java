package com.example.Triple_clone.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity(name = "userEntity")
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @OneToMany(mappedBy = "user")
    private List<Review> reviews;
}
