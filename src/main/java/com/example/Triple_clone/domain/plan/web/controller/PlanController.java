package com.example.Triple_clone.domain.plan.web.controller;

import com.example.Triple_clone.common.auth.MemberEmailAspect;
import com.example.Triple_clone.domain.plan.application.PlanFacadeService;
import com.example.Triple_clone.domain.plan.web.dto.plan.*;
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
    @ApiResponse(responseCode = "401", description = "권한 인증 오류 발생")
    @PostMapping("/plan")
    public ResponseEntity<PlanCreateDto> createPlan(
            @Parameter(description = "계획 생성 요청 정보", required = true)
            @RequestBody PlanCreateDto createDto, @MemberEmailAspect String email) {
        PlanCreateDto responseDto = service.create(createDto, email);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "계획 조회", description = "기존 계획을 단일 조회합니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @ApiResponse(responseCode = "401", description = "권한 인증 오류 발생")
    @GetMapping("/plan/{planId}")
    public ResponseEntity<PlanReadResponseDto> readPlan(
            @Parameter(description = "계획 단일 조회 요청 정보", required = true)
            @PathVariable long planId, @MemberEmailAspect String email) {
        PlanReadResponseDto responseDto = service.findPlan(planId, email);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "계획 전체 조회", description = "기존 계획을 전체 조회합니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @ApiResponse(responseCode = "401", description = "권한 인증 오류 발생")
    @GetMapping("/plans")
    public ResponseEntity<PlanReadAllResponseDto> readAllPlan(
            @Parameter(description = "누구의 계획을 조회할 것인지에 대한 user ID", required = true)
            @MemberEmailAspect String email) {
        PlanReadAllResponseDto responseDto = service.findAllPlan(email);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "계획 스타일 수정", description = "기존 계획의 여행 스타일을 수정합니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @ApiResponse(responseCode = "401", description = "권한 인증 오류 발생")
    @PutMapping("/plan/style")
    public ResponseEntity<PlanStyleUpdateDto> updateStyle(
            @Parameter(description = "계획 스타일 수정 요청 정보", required = true)
            @RequestBody PlanStyleUpdateDto updateDto, @MemberEmailAspect String email) {
        PlanStyleUpdateDto responseDto = service.updateStyle(updateDto, email);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "계획 파트너 수정", description = "기존 계획의 여행 파트너를 수정합니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @ApiResponse(responseCode = "401", description = "권한 인증 오류 발생")
    @PutMapping("/plan/partner")
    public ResponseEntity<PlanPartnerUpdateDto> updatePartner(
            @Parameter(description = "계획 파트너 수정 요청 정보", required = true)
            @RequestBody PlanPartnerUpdateDto updateDto, @MemberEmailAspect String email) {
        PlanPartnerUpdateDto responseDto = service.updatePartner(updateDto, email);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "계획 이름 및 날짜 수정", description = "기존 계획의 이름이나 날짜를 수정합니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @ApiResponse(responseCode = "401", description = "권한 인증 오류 발생")
    @PatchMapping("/plan")
    public ResponseEntity<PlanUpdateDto> updatePlan(
            @Parameter(description = "계획 이름 및 날짜 요청 정보", required = true)
            @RequestBody PlanUpdateDto updateDto, @MemberEmailAspect String email) {
        PlanUpdateDto responseDto = service.updateNameAndDate(updateDto, email);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "계획 삭제", description = "기존의 계획을 삭제합니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @ApiResponse(responseCode = "401", description = "권한 인증 오류 발생")
    @DeleteMapping("/plan")
    public ResponseEntity<PlanDto> deletePlan(
            @Parameter(description = "계획 삭제 요청 정보", required = true)
            @RequestBody PlanDto deleteDto, @MemberEmailAspect String email) {
        PlanDto responseDto = service.deletePlan(deleteDto, email);
        return ResponseEntity.ok(responseDto);
    }
}
