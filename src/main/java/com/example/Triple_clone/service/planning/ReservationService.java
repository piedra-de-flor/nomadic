package com.example.Triple_clone.service.planning;

import com.example.Triple_clone.domain.entity.DetailPlan;
import com.example.Triple_clone.dto.planning.DetailPlanDto;
import com.example.Triple_clone.service.support.FileManager;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReservationService {
    private final EntityManager entityManager;
    private final FileManager fileManager;
    public List<DetailPlanDto> findAllMyReservation(long userId) {
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

    public Map<String, Long> findAllAccommodationsSortByPrice(String local) {
        return convertListToMap(fileManager.readHotelsFromFile(local)
                .entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toList()));
    }

    public Map<String, Long> findAllAccommodations(String local) {
        return fileManager.readHotelsFromFile(local);
    }

    private Map<String, Long> convertListToMap(List<Map.Entry<String, Long>> sortedList) {
        Map<String, Long> sortedMap = new HashMap<>();
        for (Map.Entry<String, Long> entry : sortedList) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }
}
