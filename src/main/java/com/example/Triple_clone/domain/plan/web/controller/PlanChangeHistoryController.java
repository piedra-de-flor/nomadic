package com.example.Triple_clone.domain.plan.web.controller;

import com.example.Triple_clone.common.auth.MemberEmailAspect;
import com.example.Triple_clone.domain.plan.application.PlanChangeHistoryFacadeService;
import com.example.Triple_clone.domain.plan.domain.ChangeType;
import com.example.Triple_clone.domain.plan.web.dto.planhistory.PlanChangeHistoryResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "계획 변경 이력 Controller", description = "PLAN CHANGE HISTORY API")
public class PlanChangeHistoryController {
    private final PlanChangeHistoryFacadeService service;

    @Operation(summary = "계획 변경 이력 전체 조회", description = "특정 계획의 모든 변경 이력을 페이징으로 조회합니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @ApiResponse(responseCode = "401", description = "권한 인증 오류 발생")
    @GetMapping("/plan/{planId}/history")
    public ResponseEntity<Page<PlanChangeHistoryResponseDto>> getPlanHistory(
            @Parameter(description = "계획 ID", required = true)
            @PathVariable Long planId,
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @MemberEmailAspect String email) {

        Pageable pageable = PageRequest.of(page, size);
        Page<PlanChangeHistoryResponseDto> response = service.getPlanHistory(planId, email, pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "특정 타입 변경사항 조회", description = "특정 변경 타입의 이력만 페이징으로 조회합니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @ApiResponse(responseCode = "401", description = "권한 인증 오류 발생")
    @GetMapping("/plan/{planId}/history/type/{changeType}")
    public ResponseEntity<Page<PlanChangeHistoryResponseDto>> getChangesByType(
            @Parameter(description = "계획 ID", required = true)
            @PathVariable Long planId,
            @Parameter(description = "변경 타입", required = true)
            @PathVariable ChangeType changeType,
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @MemberEmailAspect String email) {

        Pageable pageable = PageRequest.of(page, size);
        Page<PlanChangeHistoryResponseDto> response = service.getChangesByType(planId, changeType, email, pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "내 활동 내역 조회", description = "내가 한 모든 변경사항을 페이징으로 조회합니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @ApiResponse(responseCode = "401", description = "권한 인증 오류 발생")
    @GetMapping("/my/activity")
    public ResponseEntity<Page<PlanChangeHistoryResponseDto>> getMyActivity(
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @MemberEmailAspect String email) {

        Pageable pageable = PageRequest.of(page, size);
        Page<PlanChangeHistoryResponseDto> response = service.getUserActivity(email, pageable);
        return ResponseEntity.ok(response);
    }
}