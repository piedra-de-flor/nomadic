package com.example.Triple_clone.domain.member.domain;

//TODO
//FIXME
// - 추후에 Spring Security 와 JWT Token 을 추가하였을 때 사용될 Role enum class
public enum Role {
    ADMIN("ADMIN"),
    USER("USER");

    public final String role;

    Role(String role) {
        this.role = role;
    }
}
