package com.example.Triple_clone.controller.recommend.manager;

import com.example.Triple_clone.dto.recommend.manager.AdminRecommendCreatePlaceDto;
import com.example.Triple_clone.dto.recommend.manager.AdminRecommendUpdatePlaceDto;
import com.example.Triple_clone.entity.Place;
import com.example.Triple_clone.service.recommend.manager.AdminRecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AdminRecommendController {
    private final AdminRecommendService service;

    @PostMapping("/recommend/admin")
    public ResponseEntity<Place> createPlace(@RequestBody @Validated AdminRecommendCreatePlaceDto createPlaceRequestDto) {
        return ResponseEntity.ok(service.createPlace(createPlaceRequestDto));
    }

    @PatchMapping("/recommend/admin")
    public ResponseEntity<Place> updatePlace(@RequestBody AdminRecommendUpdatePlaceDto updatePlaceRequestDto) {
        return ResponseEntity.ok(service.updatePlace(updatePlaceRequestDto));
    }

    @DeleteMapping("/recommend/admin")
    public ResponseEntity<Long> deletePlace(@RequestParam Long placeId) {
        return ResponseEntity.ok(service.deletePlace(placeId));
    }
}
