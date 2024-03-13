package com.example.Triple_clone.domain.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class MemberTest {
    @Test
    void 회원_업데이트_테스트() {
        Member member = new Member();
        member.update("test", "test");

        assertThat(member.getName()).isEqualTo("test");
        assertThat(member.getPassword()).isEqualTo("test");
    }
}
