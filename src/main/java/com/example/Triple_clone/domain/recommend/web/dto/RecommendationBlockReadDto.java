package com.example.Triple_clone.domain.recommend.web.dto;

import com.example.Triple_clone.common.file.Image;
import com.example.Triple_clone.domain.recommend.domain.BlockType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "추천 장소 블록 조회 응답 DTO")
public class RecommendationBlockReadDto {
    
    @Schema(description = "블록 ID", example = "1")
    private Long id;
    
    @Schema(description = "블록 타입 (TEXT 또는 IMAGE)", example = "TEXT")
    private BlockType type;
    
    @Schema(description = "텍스트 내용", example = "여행지 소개 내용")
    private String text;
    
    @Schema(description = "이미지 정보 (IMAGE 타입일 때)")
    private Image image;
    
    @Schema(description = "이미지 캡션", example = "아름다운 바다 전경")
    private String caption;
    
    @Schema(description = "블록 순서 인덱스", example = "0")
    private Integer orderIndex;
    
    public static RecommendationBlockReadDto from(com.example.Triple_clone.domain.recommend.domain.RecommendationBlock block) {
        return new RecommendationBlockReadDto(
            block.getId(),
            block.getType(),
            block.getText(),
            block.getImage(),
            block.getCaption(),
            block.getOrderIndex()
        );
    }
}
