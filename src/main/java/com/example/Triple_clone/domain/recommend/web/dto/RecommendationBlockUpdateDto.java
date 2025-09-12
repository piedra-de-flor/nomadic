package com.example.Triple_clone.domain.recommend.web.dto;

import com.example.Triple_clone.domain.recommend.domain.BlockType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "추천 장소 블록 수정 요청 DTO")
public class RecommendationBlockUpdateDto {
    
    @Schema(description = "블록 타입 (TEXT 또는 IMAGE)", example = "TEXT", required = true)
    private String type;
    
    @Schema(description = "텍스트 내용 (TEXT 타입일 때)", example = "수정된 여행지 소개 내용")
    private String text;
    
    @Schema(description = "이미지 파일 (IMAGE 타입일 때, 선택사항)")
    private MultipartFile imageFile;
    
    @Schema(description = "이미지 캡션 (IMAGE 타입일 때)", example = "수정된 이미지 캡션")
    private String caption;
    
    @Schema(description = "블록 순서 인덱스 (문자열)", example = "1", required = true)
    private String orderIndex;
}
