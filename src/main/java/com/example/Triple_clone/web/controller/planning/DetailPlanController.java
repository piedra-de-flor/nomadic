package com.example.Triple_clone.web.controller.planning;

import com.example.Triple_clone.domain.entity.DetailPlan;
import com.example.Triple_clone.dto.planning.DetailPlanDto;
import com.example.Triple_clone.dto.planning.DetailPlanUpdateDto;
import com.example.Triple_clone.dto.planning.ReservationCreateDto;
import com.example.Triple_clone.service.planning.DetailPlanFacadeService;
import com.example.Triple_clone.service.planning.DetailPlanService;
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
@Tag(name = "세부 계획 Controller", description = "DETAIL PLAN API")
public class DetailPlanController {
    private final DetailPlanFacadeService service;

    @Operation(summary = "세부 계획 생성", description = "새로운 세부 계획을 생성합니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @PostMapping("/detail-plan")
    public ResponseEntity<DetailPlanDto> create(
            @Parameter(description = "세부 계획 생성 요청 정보", required = true)
            @RequestBody DetailPlanDto detailPlanDto) {
        DetailPlanDto response = service.create(detailPlanDto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "추천 장소 계획에 추가", description = "추천 장소를 새로운 세부 계획으로 생성합니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @PostMapping("/detail-plan/redirect")
    public ResponseEntity<DetailPlanDto> create(
            @Parameter(description = "추가할 추천 장소 ID", required = true)
            @RequestParam long target,
            @Parameter(description = "추천 장소를 생성할 PLAN ID", required = true)
            @RequestParam long planId) {
        DetailPlanDto response = service.addRecommendation(target, planId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "예약 생성", description = "새로운 예약을 생성합니다 (예약 = 세부계획의 하위그룹)")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @PostMapping("/detail-plan/reservation")
    public ResponseEntity<ReservationCreateDto> create(
            @Parameter(description = "예약 생성 요청 정보", required = true)
            @RequestBody ReservationCreateDto reservationCreateDto) {
        ReservationCreateDto response = service.createReservation(reservationCreateDto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "세부 계획 전체 조회", description = "계획에 포함된 모든 세부 계획을 불러옵니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @GetMapping("/detail-plans")
    public ResponseEntity<List<DetailPlanDto>> readAll(
            @Parameter(description = "세부 계획 전체 조회 요청 정보", required = true)
            @RequestParam long planId) {
        List<DetailPlanDto> response = service.readAll(planId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "세부 계획 수정", description = "기존의 세부 계획 정보를 수정합니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @PatchMapping("/detail-plan")
    public ResponseEntity<DetailPlanDto> update(
            @Parameter(description = "세부 계획 수정 요청 정보", required = true)
            @RequestBody DetailPlanUpdateDto updateDto) {
        DetailPlanDto response = service.update(updateDto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "세부 계획 삭제", description = "기존의 회원 정보를 삭제합니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @DeleteMapping("/detailPlan")
    public ResponseEntity<DetailPlan> delete(
            @Parameter(description = "삭제할 세부 계획이 포함된 계획 ID", required = true)
            @RequestParam long planId,
            @Parameter(description = "삭제할 세부 계획 ID", required = true)
            @RequestParam long detailPlanId) {
        DetailPlan detailPlan = service.delete(planId, detailPlanId);
        return ResponseEntity.ok(detailPlan);
    }
}
