package com.example.Triple_clone.domain.recommend.web.dto;

import com.example.Triple_clone.domain.recommend.domain.RecommendationType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

@Schema(description = "추천 장소 생성 요청 DTO (Form Data 전용)")
public record RecommendationCreateDto(
        @NotBlank(message = "제목은 필수입니다")
        @Schema(description = "추천 장소 제목", example = "제주도 한라산", required = true)
        String title,
        
        @Schema(description = "부제목", example = "한국의 최고봉")
        String subTitle,
        
        @NotNull(message = "위치 정보는 필수입니다")
        @Schema(description = "위치 정보 (JSON 문자열)", example = "{\"address\":\"제주특별자치도\",\"latitude\":33.3617,\"longitude\":126.5292}", required = true)
        String location,
        
        @Schema(description = "가격 정보", example = "무료")
        String price,
        
        @NotNull(message = "타입은 필수입니다")
        @Schema(description = "추천 타입 (PLACE 또는 POST)", example = "PLACE", required = true)
        RecommendationType type,
        
        @Schema(description = "태그 목록 (쉼표로 구분)", example = "산,등산,자연")
        String tags,
        
        @Schema(description = "메인 이미지 파일")
        MultipartFile mainImage) {
}
