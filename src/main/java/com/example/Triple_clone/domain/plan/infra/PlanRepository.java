package com.example.Triple_clone.domain.plan.infra;

import com.example.Triple_clone.domain.plan.domain.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRepository extends JpaRepository<Plan, Long> {
}
