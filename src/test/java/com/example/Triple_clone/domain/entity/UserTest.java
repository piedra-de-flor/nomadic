package com.example.Triple_clone.domain.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserTest {
    @Test
    void 회원_업데이트_테스트() {
        User user = new User();
        user.update("test", "test");

        assertThat(user.getName()).isEqualTo("test");
        assertThat(user.getPassword()).isEqualTo("test");
    }
}
