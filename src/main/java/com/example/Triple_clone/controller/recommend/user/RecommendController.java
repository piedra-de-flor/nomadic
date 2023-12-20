package com.example.Triple_clone.controller.recommend.user;

import com.example.Triple_clone.dto.recommend.user.RecommendLikeDto;
import com.example.Triple_clone.dto.recommend.user.RecommendReadDto;
import com.example.Triple_clone.dto.recommend.user.RecommendWriteReviewDto;
import com.example.Triple_clone.service.recommend.user.RecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@RequiredArgsConstructor
public class RecommendController {
    private static final String REDIRECT_END_POINT_TO_PLANNING_SERVICE = "";
    private final RecommendService service;

    @GetMapping("/recommend/place/all")
    public ResponseEntity<Page<RecommendReadDto>> readAllOrderBy(
            @RequestParam(required = false, defaultValue = "") String sort,
            Pageable pageable) {
        return ResponseEntity.ok(service.findAll(sort, pageable));
    }

    @GetMapping("/recommend/place")
    public ResponseEntity<RecommendReadDto> read(@RequestParam long placeId, @RequestParam long userId) {
        RecommendReadDto recommendReadDto = service.findById(placeId, userId);
        return ResponseEntity.ok(recommendReadDto);
    }

    @PutMapping("/recommend/place/like")
    public ResponseEntity<RecommendLikeDto> like(@RequestBody RecommendLikeDto recommendLikeDto) {
        service.like(recommendLikeDto.placeId(), recommendLikeDto.userId());
        return ResponseEntity.ok(recommendLikeDto);
    }

    @PostMapping("/recommend/review")
    public ResponseEntity<RecommendWriteReviewDto> writeReview(@RequestBody RecommendWriteReviewDto writeReviewRequestDto) {
        service.writeReview(writeReviewRequestDto);
        return ResponseEntity.ok(writeReviewRequestDto);
    }

    @GetMapping("/recommend/user/plan")
    public String redirectToPlanning(@RequestParam String target, @RequestParam long placeId, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("placeId", placeId);
        return "redirect:/" + REDIRECT_END_POINT_TO_PLANNING_SERVICE + target;
    }
}
