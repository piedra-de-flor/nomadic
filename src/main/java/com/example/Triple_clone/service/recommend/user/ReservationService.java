package com.example.Triple_clone.service.recommend.user;

import com.example.Triple_clone.domain.entity.DetailPlan;
import com.example.Triple_clone.domain.entity.Member;
import com.example.Triple_clone.dto.planning.DetailPlanDto;
import com.example.Triple_clone.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class ReservationService {
    private final EntityManager entityManager;
    private final MemberRepository memberRepository;
    public List<DetailPlanDto> findAllMyReservation(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("no user entity"));

        String sql = "SELECT dp.* FROM detail_plan dp " +
                "JOIN plan p ON dp.plan_id = p.id " +
                "JOIN user_entity u ON p.user_id = u.id " +
                "WHERE u.id =" + member.getId() + " AND dp.dtype = 'R'";

        Query query = entityManager.createNativeQuery(sql, DetailPlan.class);
        List<DetailPlan> result = (List<DetailPlan>) query.getResultList();
        List<DetailPlanDto> response = new ArrayList<>();

        for (DetailPlan detailPlan : result) {
            response.add(detailPlan.toDto());
        }

        return response;
    }
}
