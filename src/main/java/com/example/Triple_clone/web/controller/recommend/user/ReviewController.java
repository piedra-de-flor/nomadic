package com.example.Triple_clone.web.controller.recommend.user;

import com.example.Triple_clone.dto.recommend.user.RecommendWriteReviewDto;
import com.example.Triple_clone.service.review.ReviewFacadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Tag(name = "리뷰 Controller", description = "REVIEW API")
public class ReviewController {
    private final ReviewFacadeService service;

    @Operation(summary = "추천 장소에 대한 리뷰 생성", description = "기존 추천 장소에 새로운 리뷰를 생성합니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @PostMapping("/recommend/review")
    public ResponseEntity<RecommendWriteReviewDto> writeReview(
            @Parameter(description = "추천 장소에 대한 리뷰 생성 정보", required = true)
            @RequestBody @Validated RecommendWriteReviewDto writeReviewRequestDto) {
        service.writeReview(writeReviewRequestDto);
        return ResponseEntity.ok(writeReviewRequestDto);
    }
}
