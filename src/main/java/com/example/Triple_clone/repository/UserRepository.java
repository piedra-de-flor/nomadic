package com.example.Triple_clone.repository;

import com.example.Triple_clone.domain.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @EntityGraph(attributePaths = "authorities") // 쿼리 수행 시 Lazy 조회가 아닌, Eager 조회로 authorities 정보를 같이가져옴
    Optional<User> findOneWithAuthoritiesByEmail(String email);
}
