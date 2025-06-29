package com.example.Triple_clone.domain.member.infra;

import com.example.Triple_clone.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    List<Member> findAllByRolesIn(List<String> roles);
}
