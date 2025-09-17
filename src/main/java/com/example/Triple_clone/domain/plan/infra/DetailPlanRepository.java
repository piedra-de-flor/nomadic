package com.example.Triple_clone.domain.plan.infra;

import com.example.Triple_clone.domain.plan.domain.DetailPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DetailPlanRepository extends JpaRepository<DetailPlan, Long> {
    @Query("select dp from DetailPlan dp join fetch dp.plan p where p.id = :planId")
    List<DetailPlan> findAllByPlanId(@Param("planId") Long planId);
}
