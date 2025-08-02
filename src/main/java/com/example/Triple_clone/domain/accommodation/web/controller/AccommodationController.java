package com.example.Triple_clone.domain.accommodation.web.controller;

import com.example.Triple_clone.domain.accommodation.application.AccommodationService;
import com.example.Triple_clone.domain.accommodation.domain.SortOption;
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

    @Operation(summary = "숙소 리스트 검색", description = "ES 기반으로 다양한 조건으로 숙소를 검색합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @ApiResponse(responseCode = "401", description = "권한 인증 오류 발생")
    @GetMapping("/accommodations")
    public ResponseEntity<List<AccommodationDto>> readAll(
            @RequestParam(required = false) String searchKeyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Float ratingMin,
            @RequestParam(required = false) Float ratingMax,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) Integer dayusePriceMin,
            @RequestParam(required = false) Integer dayusePriceMax,
            @RequestParam(required = false) Boolean dayuseAvailable,
            @RequestParam(required = false) Boolean hasDayuseDiscount,
            @RequestParam(required = false) Integer stayPriceMin,
            @RequestParam(required = false) Integer stayPriceMax,
            @RequestParam(required = false) Boolean stayAvailable,
            @RequestParam(required = false) Boolean hasStayDiscount,
            @RequestParam(required = false) Integer roomPriceMin,
            @RequestParam(required = false) Integer roomPriceMax,
            @RequestParam(required = false) Integer roomCapacityMin,
            @RequestParam(required = false) Integer roomCapacityMax,
            @RequestParam(required = false) String roomCheckoutTime,
            @RequestParam(required = false) SortOption sortOption,
            Pageable pageable
    ) {
        List<AccommodationDto> result = service.searchES(
                searchKeyword,
                category,
                ratingMin, ratingMax,
                region,
                dayusePriceMin, dayusePriceMax,
                dayuseAvailable,
                hasDayuseDiscount,
                stayPriceMin, stayPriceMax,
                stayAvailable,
                hasStayDiscount,
                roomPriceMin, roomPriceMax,
                roomCapacityMin, roomCapacityMax,
                roomCheckoutTime,
                sortOption,
                pageable
        );
        return ResponseEntity.ok(result);
    }
}
