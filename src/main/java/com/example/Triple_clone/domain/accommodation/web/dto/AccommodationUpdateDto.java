package com.example.Triple_clone.domain.accommodation.web.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccommodationUpdateDto {

    private String image;

    @Size(max = 255, message = "숙소명은 255자를 초과할 수 없습니다")
    private String name;

    @Size(max = 100, message = "카테고리는 100자를 초과할 수 없습니다")
    private String category;

    @Size(max = 50, message = "등급은 50자를 초과할 수 없습니다")
    private String grade;

    @Size(max = 100, message = "지역은 100자를 초과할 수 없습니다")
    private String region;

    @Size(max = 500, message = "주소는 500자를 초과할 수 없습니다")
    private String address;

    @Size(max = 200, message = "랜드마크 거리는 200자를 초과할 수 없습니다")
    private String landmarkDistance;

    private String intro;
    private String amenities;
    private String info;
}