package com.example.Triple_clone.web.controller.planning;

import com.example.Triple_clone.domain.entity.DetailPlan;
import com.example.Triple_clone.dto.planning.DetailPlanDto;
import com.example.Triple_clone.dto.planning.DetailPlanUpdateDto;
import com.example.Triple_clone.service.planning.DetailPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class DetailPlanController {
    private final DetailPlanService service;

    @PostMapping("/detailPlan")
    public ResponseEntity<DetailPlanDto> create(DetailPlanDto detailPlanDto) {
        DetailPlanDto response = service.create(detailPlanDto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/detailPlan")
    public ResponseEntity<DetailPlanDto> update(DetailPlanUpdateDto updateDto) {
        DetailPlanDto response = service.update(updateDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/detailPlan")
    public ResponseEntity<DetailPlan> delete(@RequestParam long planId, @RequestParam long detailPlanId) {
        DetailPlan detailPlan = service.delete(planId, detailPlanId);
        return ResponseEntity.ok(detailPlan);
    }
}
