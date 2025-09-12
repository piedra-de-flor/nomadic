package com.example.Triple_clone.domain.plan.web.controller;

import com.example.Triple_clone.common.auth.MemberEmailAspect;
import com.example.Triple_clone.domain.plan.application.PlanShareFacadeService;
import com.example.Triple_clone.domain.plan.web.dto.planshare.PlanShareCreateDto;
import com.example.Triple_clone.domain.plan.web.dto.planshare.PlanShareResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "계획 공유 Controller", description = "PLAN SHARE API")
public class PlanShareController {
    private final PlanShareFacadeService service;

    @Operation(summary = "계획 공유", description = "다른 사용자와 계획을 공유합니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @ApiResponse(responseCode = "401", description = "권한 인증 오류 발생")
    @PostMapping("/plan/share")
    public ResponseEntity<PlanShareCreateDto> sharePlan(
            @Parameter(description = "계획 공유 요청 정보", required = true)
            @RequestBody PlanShareCreateDto createDto,
            @MemberEmailAspect String email) {
        PlanShareCreateDto response = service.sharePlan(createDto, email);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "공유받은 계획 목록 조회", description = "내가 공유받은 모든 계획을 조회합니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @ApiResponse(responseCode = "401", description = "권한 인증 오류 발생")
    @GetMapping("/plan/shared")
    public ResponseEntity<List<PlanShareResponseDto>> getSharedPlans(
            @MemberEmailAspect String email) {
        List<PlanShareResponseDto> response = service.getSharedPlans(email);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "대기중인 공유 요청 조회", description = "나에게 온 대기중인 공유 요청을 조회합니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @ApiResponse(responseCode = "401", description = "권한 인증 오류 발생")
    @GetMapping("/plan/share/pending")
    public ResponseEntity<List<PlanShareResponseDto>> getPendingShares(
            @MemberEmailAspect String email) {
        List<PlanShareResponseDto> response = service.getPendingShares(email);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "계획 공유 멤버 조회", description = "특정 계획에 공유된 멤버들을 조회합니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @ApiResponse(responseCode = "401", description = "권한 인증 오류 발생")
    @GetMapping("/plan/{planId}/members")
    public ResponseEntity<List<PlanShareResponseDto>> getPlanSharedMembers(
            @Parameter(description = "계획 ID", required = true)
            @PathVariable Long planId,
            @MemberEmailAspect String email) {
        List<PlanShareResponseDto> response = service.getPlanSharedMembers(planId, email);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "공유 요청 수락", description = "공유 요청을 수락합니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @ApiResponse(responseCode = "401", description = "권한 인증 오류 발생")
    @PutMapping("/plan/share/{shareId}/accept")
    public ResponseEntity<PlanShareResponseDto> acceptShare(
            @Parameter(description = "공유 ID", required = true)
            @PathVariable Long shareId,
            @MemberEmailAspect String email) {
        PlanShareResponseDto response = service.acceptShare(shareId, email);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "공유 요청 거부", description = "공유 요청을 거부합니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @ApiResponse(responseCode = "401", description = "권한 인증 오류 발생")
    @PutMapping("/plan/share/{shareId}/reject")
    public ResponseEntity<PlanShareResponseDto> rejectShare(
            @Parameter(description = "공유 ID", required = true)
            @PathVariable Long shareId,
            @MemberEmailAspect String email) {
        PlanShareResponseDto response = service.rejectShare(shareId, email);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "공유 해제", description = "계획 공유를 해제합니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @ApiResponse(responseCode = "401", description = "권한 인증 오류 발생")
    @DeleteMapping("/plan/share/{shareId}")
    public ResponseEntity<Void> removeShare(
            @Parameter(description = "공유 ID", required = true)
            @PathVariable Long shareId,
            @MemberEmailAspect String email) {
        service.removeShare(shareId, email);
        return ResponseEntity.ok().build();
    }
}