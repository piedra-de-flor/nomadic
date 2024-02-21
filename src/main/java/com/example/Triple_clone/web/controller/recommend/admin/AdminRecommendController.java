package com.example.Triple_clone.web.controller.recommend.admin;
import com.example.Triple_clone.dto.recommend.admin.AdminRecommendCreatePlaceDto;
import com.example.Triple_clone.dto.recommend.admin.AdminRecommendUpdatePlaceDto;
import com.example.Triple_clone.domain.entity.Place;
import com.example.Triple_clone.service.recommend.admin.AdminRecommendService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AdminRecommendController {
    private final AdminRecommendService service;

    @PostMapping("/admin/recommend")
    public ResponseEntity<Place> createPlace(@RequestBody @Validated AdminRecommendCreatePlaceDto createPlaceRequestDto) {
        Place createdPlace = service.createPlace(createPlaceRequestDto);
        return ResponseEntity.ok(createdPlace);
    }

    @PatchMapping("/admin/recommend")
    public ResponseEntity<Place> updatePlace(@RequestBody @Validated AdminRecommendUpdatePlaceDto updatePlaceRequestDto) {
        Place updatedPlace = service.updatePlace(updatePlaceRequestDto);
        return ResponseEntity.ok(updatedPlace);
    }

    @DeleteMapping("/admin/recommend")
    public ResponseEntity<Long> deletePlace(@RequestParam long placeId) {
        long deletedPlaceId = service.deletePlace(placeId);
        return ResponseEntity.ok(deletedPlaceId);
    }
}
