package com.example.Triple_clone.domain.plan.infra;

import com.example.Triple_clone.domain.plan.domain.DetailPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetailPlanRepository extends JpaRepository<DetailPlan, Long> {
}
