package com.example.Triple_clone.web.controller.reservation;

import com.example.Triple_clone.dto.planning.DetailPlanDto;
import com.example.Triple_clone.service.planning.ReservationService;
import com.example.Triple_clone.service.support.FileManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name = "숙소 예약 Controller", description = "RESERVATION API")
public class ReservationController {
    private final ReservationService reservationService;

    @Operation(summary = "숙소 리스트 전체 조회(지역별)", description = "지역별에 맞는 숙소를 전체 조회합니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @GetMapping("/reservations")
    public ResponseEntity<Map<String, Long>> readAll(
            @Parameter(description = "원하는 지역 이름", required = true)
            @RequestParam String local) {
        return ResponseEntity.ok(reservationService.findAllAccommodations(local));
    }

    @Operation(summary = "숙소 리스트 전체 조회(지역별 및 가격순 정렬)", description = "지역별에 맞는 숙소를 가격순으로 전체 조회합니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @GetMapping("/reservations/price")
    public ResponseEntity<Map<String, Long>> readAllOrderByPrice(
            @Parameter(description = "원하는 지역 이름", required = true)
            @RequestParam String local) {
        return ResponseEntity.ok(reservationService.findAllAccommodationsSortByPrice(local));
    }

    @Operation(summary = "내가 예약한 숙소 조회", description = "내가 예약한 숙소들을 조회합니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @GetMapping("/myReservations")
    public ResponseEntity<List<DetailPlanDto>> findAllMyReservations(
            @Parameter(description = "내 예약 조회 요청 정보 (내 USER ID)", required = true)
            @RequestParam long userId) {
        List<DetailPlanDto> response = reservationService.findAllMyReservation(userId);
        return ResponseEntity.ok(response);
    }
}
