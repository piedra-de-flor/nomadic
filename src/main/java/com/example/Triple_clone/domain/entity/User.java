package com.example.Triple_clone.domain.entity;

import com.example.Triple_clone.domain.entity.Review;
import com.example.Triple_clone.domain.vo.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "userEntity")
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;

    //TODO
    //FIXME
    // - 추루에 Spring Security 와 함께 사용될 Role 필드
    // - 현재는 Spring filter만을 이용해서 token 검증
    // private Role role;

    @OneToMany(mappedBy = "user")
    private List<Review> reviews;

    public User(String email, String password, Role role) {
    }
}
