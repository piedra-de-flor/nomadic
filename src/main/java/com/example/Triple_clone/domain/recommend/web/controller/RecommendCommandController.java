package com.example.Triple_clone.domain.recommend.web.controller;
import com.example.Triple_clone.common.auth.MemberEmailAspect;
import com.example.Triple_clone.domain.recommend.application.RecommendCommandService;
import com.example.Triple_clone.domain.recommend.domain.Recommendation;
import com.example.Triple_clone.domain.recommend.domain.RecommendationBlock;
import com.example.Triple_clone.domain.recommend.web.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "장소 추천 command Controller", description = "RECOMMEND COMMAND API")
public class RecommendCommandController {
    private final RecommendCommandService service;

    @Operation(summary = "추천 장소 생성", description = "새로운 추천 장소를 생성합니다 (이미지 파일 포함)")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @ApiResponse(responseCode = "401", description = "권한 인증 오류 발생")
    @PostMapping("/recommend")
    public ResponseEntity<Long> createPlace(
            @Parameter(description = "추천 장소 생성 요청 정보 (multipart/form-data)", required = true)
            @ModelAttribute @Validated RecommendationCreateDto createPlaceRequestDto,
            @MemberEmailAspect String email) {
        Recommendation createdRecommendation = service.createRecommendation(createPlaceRequestDto, email);
        return ResponseEntity.ok(createdRecommendation.getId());
    }

    @Operation(summary = "추천 장소 이미지 추가", description = "추천 장소에 이미지를 추가합니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @ApiResponse(responseCode = "401", description = "권한 인증 오류 발생")
    @PostMapping("/recommend/image")
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
    @PatchMapping("/recommend")
    public ResponseEntity<RecommendReadDto> updatePlace(
            @Parameter(description = "추천 장소 수정 요청 정보", required = true)
            @RequestBody @Validated RecommendationUpdateDto updatePlaceRequestDto,
            @MemberEmailAspect String email) {
        RecommendReadDto response = service.updateRecommendation(updatePlaceRequestDto, email);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "추천 장소 삭제", description = "기존 추천 장소를 삭제합니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @ApiResponse(responseCode = "401", description = "권한 인증 오류 발생")
    @DeleteMapping("/recommend")
    public ResponseEntity<Long> deletePlace(
            @Parameter(description = "추천 장소 삭제 요청 정보", required = true)
            @RequestParam long placeId,
            @MemberEmailAspect String email) {
        long deletedPlaceId = service.deleteRecommendation(placeId, email);
        return ResponseEntity.ok(deletedPlaceId);
    }

    @Operation(summary = "블록 추가", description = "추천 장소에 새로운 블록을 추가합니다 (이미지 파일 포함)")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @ApiResponse(responseCode = "401", description = "권한 인증 오류 발생")
    @PostMapping("/recommend/{recommendationId}/blocks")
    public ResponseEntity<RecommendationBlockReadDto> addBlock(
            @Parameter(description = "추천 장소 ID", required = true)
            @PathVariable Long recommendationId,
            @Parameter(description = "추가할 블록 정보 (multipart/form-data)", required = true)
            @ModelAttribute RecommendationBlockCreateDto createDto,
            @MemberEmailAspect String email) {
        log.info("addBlock Controller 호출됨 - recommendationId: {}, createDto: {}", recommendationId, createDto);
        try {
            RecommendationBlock createdBlock = service.addBlock(recommendationId, createDto, email);
            log.info("addBlock 성공 - blockId: {}", createdBlock.getId());
            return ResponseEntity.ok(RecommendationBlockReadDto.from(createdBlock));
        } catch (Exception e) {
            log.error("addBlock 실패 - recommendationId: {}, error: {}", recommendationId, e.getMessage(), e);
            throw e;
        }
    }

    @Operation(summary = "블록 수정", description = "기존 블록을 수정합니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @ApiResponse(responseCode = "401", description = "권한 인증 오류 발생")
    @PatchMapping("/recommend/blocks/{blockId}")
    public ResponseEntity<RecommendationBlockUpdateDto> updateBlock(
            @Parameter(description = "블록 ID", required = true)
            @PathVariable Long blockId,
            @Parameter(description = "수정할 블록 정보", required = true)
            @ModelAttribute RecommendationBlockUpdateDto updateDto,
            @MemberEmailAspect String email) {
        RecommendationBlockUpdateDto block = service.updateBlock(blockId, updateDto, email);
        return ResponseEntity.ok(block);
    }

    @Operation(summary = "블록 삭제", description = "기존 블록을 삭제합니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @ApiResponse(responseCode = "401", description = "권한 인증 오류 발생")
    @DeleteMapping("/recommend/blocks/{blockId}")
    public ResponseEntity<Void> removeBlock(
            @Parameter(description = "블록 ID", required = true)
            @PathVariable Long blockId,
            @MemberEmailAspect String email) {
        service.removeBlock(blockId, email);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "추천 장소에 대한 좋아요", description = "기존 추천 장소에 좋아요 혹은 좋아요 취소를 합니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @ApiResponse(responseCode = "401", description = "권한 인증 오류 발생")
    @PutMapping("/recommendation/like")
    public ResponseEntity<Long> toggleLike(
            @Parameter(description = "추천 장소 좋아요 요청 정보", required = true)
            @RequestParam long recommendationId, @RequestParam long memberId) {
        service.toggleLike(recommendationId, memberId);
        return ResponseEntity.ok(recommendationId);
    }
}
