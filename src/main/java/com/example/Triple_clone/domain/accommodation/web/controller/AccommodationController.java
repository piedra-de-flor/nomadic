package com.example.Triple_clone.domain.accommodation.web.controller;

import com.example.Triple_clone.domain.accommodation.application.AccommodationService;
import com.example.Triple_clone.domain.accommodation.web.dto.AccommodationDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class AccommodationController {
    private final AccommodationService service;

    @Operation(summary = "숙소 리스트 전체 조회(지역별)", description = "지역별에 맞는 숙소를 전체 조회합니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @GetMapping("/accommodations")
    public ResponseEntity<List<AccommodationDto>> readAll(
            @Parameter(description = "원하는 지역 이름", required = true)
            @RequestParam String local,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String discountRate,
            @RequestParam(required = false) String startLentPrice,
            @RequestParam(required = false) String endLentPrice,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String score,
            @RequestParam(required = false) String lentStatus,
            @RequestParam(required = false) String enterTime,
            @RequestParam(required = false) String startLodgmentPrice,
            @RequestParam(required = false) String endLodgmentPrice,
            @RequestParam(required = false) String lodgmentStatus,
            Pageable pageable) {
        return ResponseEntity.ok(service.searchES(local,
                name,
                discountRate,
                startLentPrice,
                endLentPrice,
                category,
                score,
                lentStatus,
                enterTime,
                startLodgmentPrice,
                endLodgmentPrice,
                lodgmentStatus,
                pageable));
    }
}
