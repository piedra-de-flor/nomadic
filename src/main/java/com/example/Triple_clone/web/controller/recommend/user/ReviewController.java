package com.example.Triple_clone.web.controller.recommend.user;

import com.example.Triple_clone.dto.recommend.user.RecommendWriteReviewDto;
import com.example.Triple_clone.service.review.ReviewFacadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@Tag(name = "리뷰 Controller", description = "REVIEW API")
public class ReviewController {
    private final ReviewFacadeService service;

    @Operation(summary = "추천 장소에 대한 리뷰 생성", description = "기존 추천 장소에 새로운 리뷰를 생성합니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @PostMapping("/recommendation/review")
    public ResponseEntity<RecommendWriteReviewDto> writeReview(
            @Parameter(description = "추천 장소에 대한 리뷰 생성 정보", required = true)
            @RequestBody @Validated RecommendWriteReviewDto writeReviewRequestDto) {
        service.writeReview(writeReviewRequestDto);
        return ResponseEntity.ok(writeReviewRequestDto);
    }

    @Operation(summary = "리뷰 사진 추가", description = "리뷰에 사진을 추가합니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @ApiResponse(responseCode = "401", description = "권한 인증 오류 발생")
    @PostMapping("/admin/recommend/image")
    public ResponseEntity<Long> setMainImageOfRecommendation(
            @Parameter(description = "이미지를 추가할 리뷰 ID", required = true)
            @RequestParam Long recommendationId,
            @Parameter(description = "업로드할 이미지", required = true)
            @RequestPart MultipartFile image) {
        Long response = service.setImageOfReview(recommendationId, image);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "리뷰 사진 조회", description = "리뷰의 사진을 가져옵니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @ApiResponse(responseCode = "401", description = "권한 인증 오류 발생")
    @GetMapping("/recommendation/image")
    public ResponseEntity<byte[]> readImage(
            @Parameter(description = "이미지를 로딩할 리뷰 ID", required = true)
            @RequestParam long reviewId) {
        byte[] response = service.loadImageAsResource(reviewId);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(response);
    }
}
