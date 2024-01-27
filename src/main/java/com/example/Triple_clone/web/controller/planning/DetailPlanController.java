package com.example.Triple_clone.web.controller.planning;

import com.example.Triple_clone.domain.entity.DetailPlan;
import com.example.Triple_clone.dto.planning.DetailPlanDto;
import com.example.Triple_clone.dto.planning.DetailPlanUpdateDto;
import com.example.Triple_clone.service.planning.DetailPlanFacadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@RequiredArgsConstructor
public class DetailPlanController {
    private final DetailPlanFacadeService service;

    @PostMapping("/detailPlan")
    public String create(@RequestBody DetailPlanDto detailPlanDto, RedirectAttributes redirectAttributes) {
        DetailPlanDto response = service.create(detailPlanDto);
        redirectAttributes.addAttribute("planId", response.planId());
        redirectAttributes.addAttribute("name", response.location().getName());
        redirectAttributes.addAttribute("latitude", response.location().getLatitude());
        redirectAttributes.addAttribute("longitude", response.location().getLongitude());

        return "redirect:/showMap";
    }

    @PatchMapping("/detailPlan")
    public ResponseEntity<DetailPlanDto> update(@RequestBody DetailPlanUpdateDto updateDto) {
        DetailPlanDto response = service.update(updateDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/detailPlan")
    public ResponseEntity<DetailPlan> delete(@RequestParam long planId, @RequestParam long detailPlanId) {
        DetailPlan detailPlan = service.delete(planId, detailPlanId);
        return ResponseEntity.ok(detailPlan);
    }
}
