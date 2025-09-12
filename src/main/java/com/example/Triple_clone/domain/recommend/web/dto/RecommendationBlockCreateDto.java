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
@Schema(description = "추천 장소 블록 생성 요청 DTO")
public class RecommendationBlockCreateDto {
    
    @Schema(description = "블록 타입 (TEXT 또는 IMAGE)", example = "TEXT", required = true)
    private String type;
    
    @Schema(description = "텍스트 내용 (TEXT 타입일 때)", example = "여행지 소개 내용")
    private String text;
    
    @Schema(description = "이미지 파일 (IMAGE 타입일 때, multipart/form-data로 전송)")
    private MultipartFile imageFile;
    
    @Schema(description = "이미지 캡션 (IMAGE 타입일 때)", example = "아름다운 바다 전경")
    private String caption;
    
    @Schema(description = "블록 순서 인덱스 (문자열)", example = "0", required = true)
    private String orderIndex;
    
    @Override
    public String toString() {
        return "RecommendationBlockCreateDto{" +
                "type='" + type + '\'' +
                ", text='" + text + '\'' +
                ", imageFile=" + (imageFile != null ? imageFile.getOriginalFilename() : "null") +
                ", caption='" + caption + '\'' +
                ", orderIndex='" + orderIndex + '\'' +
                '}';
    }
}
