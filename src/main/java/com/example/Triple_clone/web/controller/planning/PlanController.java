package com.example.Triple_clone.web.controller.planning;

import com.example.Triple_clone.dto.planning.*;
import com.example.Triple_clone.service.planning.PlanFacadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "계획 Controller", description = "PLAN API")
public class PlanController {
    private final PlanFacadeService service;

    @Operation(summary = "계획 생성", description = "새로운 계획을 생성합니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @PostMapping("/plan")
    public ResponseEntity<PlanCreateDto> createPlan(
            @Parameter(description = "계획 생성 요청 정보", required = true)
            @RequestBody PlanCreateDto createDto) {
        PlanCreateDto responseDto = service.create(createDto);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "계획 조회", description = "기존 계획을 단일 조회합니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @GetMapping("/plan")
    public ResponseEntity<PlanReadResponseDto> readPlan(
            @Parameter(description = "계획 단일 조회 요청 정보", required = true)
            @RequestBody PlanDto readRequestDto) {
        PlanReadResponseDto responseDto = service.findPlan(readRequestDto);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "계획 전체 조회", description = "기존 계획을 전체 조회합니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @GetMapping("/plans")
    public ResponseEntity<PlanReadAllResponseDto> readAllPlan(
            @Parameter(description = "누구의 계획을 조회할 것인지에 대한 user ID", required = true)
            @RequestParam long userId) {
        PlanReadAllResponseDto responseDto = service.findAllPlan(userId);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "계획 스타일 수정", description = "기존 계획의 여행 스타일을 수정합니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @PutMapping("/plan/style")
    public ResponseEntity<PlanStyleUpdateDto> updateStyle(
            @Parameter(description = "계획 스타일 수정 요청 정보", required = true)
            @RequestBody PlanStyleUpdateDto updateDto) {
        PlanStyleUpdateDto responseDto = service.updateStyle(updateDto);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "계획 파트너 수정", description = "기존 계획의 여행 파트너를 수정합니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @PutMapping("/plan/partner")
    public ResponseEntity<PlanPartnerUpdateDto> updatePartner(
            @Parameter(description = "계획 파트너 수정 요청 정보", required = true)
            @RequestBody PlanPartnerUpdateDto updateDto) {
        PlanPartnerUpdateDto responseDto = service.updatePartner(updateDto);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "계획 삭제", description = "기존의 계획을 삭제합니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @DeleteMapping("/plan")
    public ResponseEntity<PlanDto> deletePlan(
            @Parameter(description = "계획 삭제 요청 정보", required = true)
            @RequestBody PlanDto deleteDto) {
        PlanDto responseDto = service.deletePlan(deleteDto);
        return ResponseEntity.ok(responseDto);
    }
}
