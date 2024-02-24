package com.example.Triple_clone.service.reservation;

import com.example.Triple_clone.domain.entity.DetailPlan;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReservationFacadeService {
    private final EntityManager entityManager;
    public List<DetailPlan> findAllReservation(long userId) {
        String sql = "SELECT dp.* FROM detail_plan dp " +
                "JOIN plan p ON dp.plan_id = p.id " +
                "JOIN user_entity u ON p.user_id = u.id " +
                "WHERE u.id =" + userId + " AND dp.dtype = 'R'";

        Query query = entityManager.createNativeQuery(sql, DetailPlan.class);
        return (List<DetailPlan>) query.getResultList();
    }
}
