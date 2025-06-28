package com.example.Triple_clone.domain.recommend;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Tag(name = "장소 추천(관리자) Controller", description = "RECOMMEND ADMIN API")
public class AdminRecommendController {
    private final AdminRecommendService service;

    @Operation(summary = "추천 장소 생성", description = "새로운 추천 장소를 생성합니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @ApiResponse(responseCode = "401", description = "권한 인증 오류 발생")
    @PostMapping("/admin/recommend")
    public ResponseEntity<Recommendation> createPlace(
            @Parameter(description = "추천 장소 생성 요청 정보", required = true)
            @RequestBody @Validated AdminRecommendCreateRecommendationDto createPlaceRequestDto) {
        Recommendation createdRecommendation = service.createRecommendation(createPlaceRequestDto);
        return ResponseEntity.ok(createdRecommendation);
    }

    @Operation(summary = "추천 장소 이미지 추가", description = "추천 장소에 이미지를 추가합니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @ApiResponse(responseCode = "401", description = "권한 인증 오류 발생")
    @PostMapping("/admin/recommend/image")
    public ResponseEntity<Long> setMainImageOfRecommendation(
            @Parameter(description = "이미지를 추가할 추천 장소 ID", required = true)
            @RequestParam Long recommendationId,
            @Parameter(description = "추가할 이미지", required = true)
            @RequestPart MultipartFile image) {
        Long response = service.setMainImageOfRecommendation(recommendationId, image);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "추천 장소 수정", description = "기존 추천 장소를 수정합니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @ApiResponse(responseCode = "401", description = "권한 인증 오류 발생")
    @PatchMapping("/admin/recommend")
    public ResponseEntity<Recommendation> updatePlace(
            @Parameter(description = "추천 장소 수정 요청 정보", required = true)
            @RequestBody @Validated AdminRecommendUpdateRecommendationDto updatePlaceRequestDto) {
        Recommendation updatedRecommendation = service.updateRecommendation(updatePlaceRequestDto);
        return ResponseEntity.ok(updatedRecommendation);
    }

    @Operation(summary = "추천 장소 삭제", description = "기존 추천 장소를 삭제합니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @ApiResponse(responseCode = "401", description = "권한 인증 오류 발생")
    @DeleteMapping("/admin/recommend")
    public ResponseEntity<Long> deletePlace(
            @Parameter(description = "추천 장소 삭제 요청 정보", required = true)
            @RequestParam long placeId) {
        long deletedPlaceId = service.deleteRecommendation(placeId);
        return ResponseEntity.ok(deletedPlaceId);
    }
}
