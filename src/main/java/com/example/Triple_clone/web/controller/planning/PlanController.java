package com.example.Triple_clone.web.controller.planning;

import com.example.Triple_clone.dto.planning.*;
import com.example.Triple_clone.service.planning.PlanFacadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PlanController {
    private final PlanFacadeService service;

    @PostMapping("/plan")
    public ResponseEntity<PlanCreateDto> createPlan(@RequestBody PlanCreateDto createDto) {
        PlanCreateDto responseDto = service.createPlan(createDto);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/plan")
    public ResponseEntity<PlanReadResponseDto> readPlan(@RequestBody PlanDto readRequestDto) {
        PlanReadResponseDto responseDto = service.findPlan(readRequestDto);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/plans")
    public ResponseEntity<PlanReadAllResponseDto> readPlan(@RequestParam long userId) {
        PlanReadAllResponseDto responseDto = service.findAllPlan(userId);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/plan/style")
    public ResponseEntity<PlanStyleUpdateDto> updateStyle(@RequestBody PlanStyleUpdateDto updateDto) {
        PlanStyleUpdateDto responseDto = service.updateStyle(updateDto);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/plan/partner")
    public ResponseEntity<PlanPartnerUpdateDto> updatePartner(@RequestBody PlanPartnerUpdateDto updateDto) {
        PlanPartnerUpdateDto responseDto = service.updatePartner(updateDto);
        return ResponseEntity.ok(responseDto);
    }
}
