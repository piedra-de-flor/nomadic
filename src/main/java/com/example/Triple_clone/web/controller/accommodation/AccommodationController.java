package com.example.Triple_clone.web.controller.accommodation;

import com.example.Triple_clone.dto.accommodation.AccommodationDto;
import com.example.Triple_clone.dto.accommodation.AccommodationsSaveDto;
import com.example.Triple_clone.service.accommodation.AccommodationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            @RequestParam(required = false) String startLentPrice,
            @RequestParam(required = false) String endLentPrice,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String score,
            @RequestParam(required = false) String lentStatus,
            @RequestParam(required = false) String enterTime,
            @RequestParam(required = false) String discountRate,
            @RequestParam(required = false) String startTotalPrice,
            @RequestParam(required = false) String endTotalPrice,
            Pageable pageable) {
        return ResponseEntity.ok(service.readAll(local,
                name,
                startLentPrice,
                endLentPrice,
                category,
                score,
                lentStatus,
                enterTime,
                discountRate,
                startTotalPrice,
                endTotalPrice,
                pageable));
    }

  @Operation(summary = "숙소 리스트 전체 저장", description = "지역별에 맞는 숙소를 text 파일에서 읽어 저장합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @PostMapping("/accommodations")
    public ResponseEntity<List<AccommodationDto>> saveAll(
          @Parameter(description = "원하는 지역 이름", required = true)
            @RequestBody AccommodationsSaveDto accommodationsSaveDto) {
        System.out.println("CI test");
        return ResponseEntity.ok(service.saveAllAccommodations(accommodationsSaveDto.local()));
    }
}
