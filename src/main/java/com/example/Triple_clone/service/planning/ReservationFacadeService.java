package com.example.Triple_clone.service.planning;

import com.example.Triple_clone.domain.entity.DetailPlan;
import com.example.Triple_clone.dto.planning.DetailPlanDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReservationFacadeService {
    private final EntityManager entityManager;
    public List<DetailPlanDto> findAllReservation(long userId) {
        String sql = "SELECT dp.* FROM detail_plan dp " +
                "JOIN plan p ON dp.plan_id = p.id " +
                "JOIN user_entity u ON p.user_id = u.id " +
                "WHERE u.id =" + userId + " AND dp.dtype = 'R'";

        Query query = entityManager.createNativeQuery(sql, DetailPlan.class);
        List<DetailPlan> result = (List<DetailPlan>) query.getResultList();
        List<DetailPlanDto> response = new ArrayList<>();

        for (DetailPlan detailPlan : result) {
            response.add(detailPlan.toDto());
        }

        return response;
    }
}
