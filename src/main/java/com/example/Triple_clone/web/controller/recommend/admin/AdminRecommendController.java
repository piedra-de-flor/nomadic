package com.example.Triple_clone.web.controller.recommend.admin;
import com.example.Triple_clone.dto.recommend.admin.AdminRecommendCreateRecommendationDto;
import com.example.Triple_clone.dto.recommend.admin.AdminRecommendUpdateRecommendationDto;
import com.example.Triple_clone.domain.entity.Recommendation;
import com.example.Triple_clone.service.recommend.admin.AdminRecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AdminRecommendController {
    private final AdminRecommendService service;

    @PostMapping("/admin/recommend")
    public ResponseEntity<Recommendation> createPlace(@RequestBody @Validated AdminRecommendCreateRecommendationDto createPlaceRequestDto) {
        Recommendation createdRecommendation = service.createRecommendation(createPlaceRequestDto);
        return ResponseEntity.ok(createdRecommendation);
    }

    @PatchMapping("/admin/recommend")
    public ResponseEntity<Recommendation> updatePlace(@RequestBody @Validated AdminRecommendUpdateRecommendationDto updatePlaceRequestDto) {
        Recommendation updatedRecommendation = service.updateRecommendation(updatePlaceRequestDto);
        return ResponseEntity.ok(updatedRecommendation);
    }

    @DeleteMapping("/admin/recommend")
    public ResponseEntity<Long> deletePlace(@RequestParam long placeId) {
        long deletedPlaceId = service.deleteRecommendation(placeId);
        return ResponseEntity.ok(deletedPlaceId);
    }
}
