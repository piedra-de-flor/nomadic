package com.example.Triple_clone.web.controller.recommend.user;

import com.example.Triple_clone.dto.recommend.user.RecommendLikeDto;
import com.example.Triple_clone.dto.recommend.user.RecommendReadDto;
import com.example.Triple_clone.service.recommend.user.RecommendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    @GetMapping("/recommend/places")
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
    @GetMapping("/recommend/place")
    public ResponseEntity<RecommendReadDto> read(
            @Parameter(description = "추천 장소 단일 조회 요청 정보 (추천 장소 ID)", required = true)
            @RequestParam long placeId,
            @Parameter(description = "추천 장소 단일 조회 요청 정보 (유저 ID)", required = true)
            @RequestParam long userId) {
        RecommendReadDto recommendReadDto = service.findById(placeId, userId);
        return ResponseEntity.ok(recommendReadDto);
    }

    @Operation(summary = "추천 장소에 대한 좋아요", description = "기존 추천 장소에 좋아요 혹은 좋아요 취소를 합니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @PutMapping("/recommend/place/like")
    public ResponseEntity<RecommendLikeDto> like(
            @Parameter(description = "추천 장소 좋아요 요청 정보", required = true)
            @RequestBody RecommendLikeDto recommendLikeDto) {
        service.like(recommendLikeDto.placeId(), recommendLikeDto.userId());
        return ResponseEntity.ok(recommendLikeDto);
    }

    @Operation(summary = "추천 장소를 내 계획에 포함", description = "기존 추천 장소를 내 기존 계획에 포함합니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @GetMapping("/recommend/user/plan")
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
