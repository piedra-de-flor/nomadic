package com.example.Triple_clone.web.controller.recommend.admin;
import com.example.Triple_clone.dto.recommend.admin.AdminRecommendCreatePlaceDto;
import com.example.Triple_clone.dto.recommend.admin.AdminRecommendUpdatePlaceDto;
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
    public ResponseEntity<Recommendation> createPlace(@RequestBody @Validated AdminRecommendCreatePlaceDto createPlaceRequestDto) {
        Recommendation createdRecommendation = service.createPlace(createPlaceRequestDto);
        return ResponseEntity.ok(createdRecommendation);
    }

    @PatchMapping("/admin/recommend")
    public ResponseEntity<Recommendation> updatePlace(@RequestBody @Validated AdminRecommendUpdatePlaceDto updatePlaceRequestDto) {
        Recommendation updatedRecommendation = service.updatePlace(updatePlaceRequestDto);
        return ResponseEntity.ok(updatedRecommendation);
    }

    @DeleteMapping("/admin/recommend")
    public ResponseEntity<Long> deletePlace(@RequestParam long placeId) {
        long deletedPlaceId = service.deletePlace(placeId);
        return ResponseEntity.ok(deletedPlaceId);
    }
}
