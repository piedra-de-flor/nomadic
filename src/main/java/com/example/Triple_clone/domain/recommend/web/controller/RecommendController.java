package com.example.Triple_clone.domain.recommend.web.controller;

import com.example.Triple_clone.common.auth.MemberEmailAspect;
import com.example.Triple_clone.domain.recommend.application.RecommendService;
import com.example.Triple_clone.domain.recommend.web.dto.RecommendLikeDto;
import com.example.Triple_clone.domain.recommend.web.dto.RecommendReadDto;
import com.example.Triple_clone.domain.recommend.web.dto.RecommendReadTop10Dto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "장소 추천(사용자) Controller", description = "RECOMMEND API")
public class RecommendController {
    private static final String REDIRECT_END_POINT_TO_PLANNING_SERVICE = "/detailPlan/redirect";
    private final RecommendService service;

    @Operation(summary = "추천 장소 전체 조회 (선택적 정렬)", description = "자신이 원하는 정렬순에 맞춰 추천 장소를 전체 조회합니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @ApiResponse(responseCode = "401", description = "권한 인증 오류 발생")
    @GetMapping("/recommendations")
    public ResponseEntity<Page<RecommendReadDto>> readAllOrderBy(
            @Parameter(description = "원하는 정렬순 (날짜별, 이름별)", required = true)
            @RequestParam(required = false, defaultValue = "") String sort,
            Pageable pageable) {
        return ResponseEntity.ok(service.findAll(sort, pageable));
    }

    @Operation(summary = "추천 장소 단일 조회", description = "기존 추천 장소를 단일 조회합니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @ApiResponse(responseCode = "401", description = "권한 인증 오류 발생")
    @GetMapping("/recommendation")
    public ResponseEntity<RecommendReadDto> read(
            @Parameter(description = "추천 장소 단일 조회 요청 정보 (추천 장소 ID)", required = true)
            @RequestParam long placeId,
            @MemberEmailAspect String email) {
        RecommendReadDto recommendReadDto = service.findById(placeId, email);
        return ResponseEntity.ok(recommendReadDto);
    }

    @Operation(summary = "추천 장소 이미지 조회", description = "기존 추천 장소의 이미지를 가져옵니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @ApiResponse(responseCode = "401", description = "권한 인증 오류 발생")
    @GetMapping("/recommendation/image")
    public ResponseEntity<byte[]> readImage(
            @Parameter(description = "이미지를 가져올 추천 장소 ID", required = true)
            @RequestParam long recommendationId) {
        byte[] response = service.loadImageAsResource(recommendationId);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(response);
    }

    @Operation(summary = "추천 장소에 대한 좋아요", description = "기존 추천 장소에 좋아요 혹은 좋아요 취소를 합니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @ApiResponse(responseCode = "401", description = "권한 인증 오류 발생")
    @PutMapping("/recommendation/like")
    public ResponseEntity<RecommendLikeDto> like(
            @Parameter(description = "추천 장소 좋아요 요청 정보", required = true)
            @RequestBody RecommendLikeDto recommendLikeDto) {
        service.like(recommendLikeDto.placeId(), recommendLikeDto.userId());
        return ResponseEntity.ok(recommendLikeDto);
    }

    @Operation(summary = "인기가 많은 상위 10개의 추천 장소 보기", description = "좋아요 상위 10개의 추천 장소들에 대해 조회를 합니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @ApiResponse(responseCode = "401", description = "권한 인증 오류 발생")
    @PutMapping("/recommendations/top10")
    public ResponseEntity<List<RecommendReadTop10Dto>> readTop10() {
        List<RecommendReadTop10Dto> response = service.findTop10();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "추천 장소를 내 계획에 포함", description = "기존 추천 장소를 내 기존 계획에 포함합니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @ApiResponse(responseCode = "401", description = "권한 인증 오류 발생")
    @GetMapping("/recommendation/user/plan")
    public String redirectToPlanning(
            @Parameter(description = "타겟 계획 ID", required = true)
            @RequestParam long target,
            @Parameter(description = "계획에 추가할 추천 장소 ID", required = true)
            @RequestParam long placeId,
            RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("placeId", placeId);
        redirectAttributes.addAttribute("planId", target);
        return "redirect:/" + REDIRECT_END_POINT_TO_PLANNING_SERVICE;
    }
}
