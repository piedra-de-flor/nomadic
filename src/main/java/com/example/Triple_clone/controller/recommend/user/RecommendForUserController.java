package com.example.Triple_clone.controller.recommend.user;

import com.example.Triple_clone.dto.recommend.user.RecommendForUserLikeRequestDto;
import com.example.Triple_clone.dto.recommend.user.RecommendForUserReadResponseDto;
import com.example.Triple_clone.dto.recommend.user.RecommendForUserWriteReviewRequestDto;
import com.example.Triple_clone.service.recommend.user.RecommendForUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@RequiredArgsConstructor
public class RecommendForUserController {
    private static final String REDIRECT_END_POINT_TO_PLANNING_SERVICE = "";
    private final RecommendForUserService service;

    @GetMapping("/recommend/user/all")
    public ResponseEntity<Page<RecommendForUserReadResponseDto>> readAllOrderBy(
            @RequestParam(required = false, defaultValue = "") String sort,
            Pageable pageable) {
        return ResponseEntity.ok(service.findAll(sort, pageable));
    }

    @GetMapping("/recommend/user")
    public ResponseEntity<RecommendForUserReadResponseDto> read(@RequestParam long placeId, @RequestParam long userId) {
        RecommendForUserReadResponseDto recommendForUserReadResponseDto = service.findById(placeId, userId);
        return ResponseEntity.ok(recommendForUserReadResponseDto);
    }

    @PutMapping("/recommend/user/like")
    public ResponseEntity<Void> like(@RequestBody RecommendForUserLikeRequestDto recommendForUserLikeRequestDto) {
        service.like(recommendForUserLikeRequestDto.placeId(), recommendForUserLikeRequestDto.userId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/recommend/user/review")
    public ResponseEntity<Void> writeReview(@RequestBody RecommendForUserWriteReviewRequestDto writeReviewRequestDto) {
        service.writeReview(writeReviewRequestDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/recommend/user/redirect/plan")
    public String redirectToPlanning(@RequestParam String target, @RequestParam long placeId, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("placeId", placeId);
        return "redirect:/" + REDIRECT_END_POINT_TO_PLANNING_SERVICE + target;
    }
}
