package com.example.Triple_clone.web.controller.planning;

import com.example.Triple_clone.dto.membership.UserJoinRequestDto;
import com.example.Triple_clone.dto.membership.UserResponseDto;
import com.example.Triple_clone.dto.planning.PlanCreateDto;
import com.example.Triple_clone.dto.planning.PlanDto;
import com.example.Triple_clone.service.planning.PlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PlanController {
    private final PlanService service;

    @PostMapping("/plan")
    public ResponseEntity<PlanDto> createPlan(@RequestBody @Valid PlanCreateDto createDto) {
        PlanDto responseDto = service.createPlan(createDto);
        return ResponseEntity.ok(responseDto);
    }
}
