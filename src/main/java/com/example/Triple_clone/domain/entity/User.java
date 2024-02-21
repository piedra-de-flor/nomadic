package com.example.Triple_clone.domain.entity;

import com.example.Triple_clone.domain.vo.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "userEntity")
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String email;
    private String password;

    //TODO
    //FIXME
    // - 추루에 Spring Security 와 함께 사용될 Role 필드
    // - 현재는 Spring filter만을 이용해서 token 검증
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Review> reviews;

    @OneToMany(mappedBy = "user")
    private List<Plan> plans;

    @Builder
    public User(String name, String email, String password, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.reviews = new ArrayList<>();
        this.plans = new ArrayList<>();
    }

    public void update(String name, String password) {
        this.name = name;
        this.password = password;
    }
}
