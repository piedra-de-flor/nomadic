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

    @PostMapping("/admin/recommend")
    public ResponseEntity<Place> createPlace(@RequestBody @Validated AdminRecommendCreatePlaceDto createPlaceRequestDto) {
        Place createdPlace = service.createPlace(createPlaceRequestDto);
        return ResponseEntity.ok(createdPlace);
    }

    @PatchMapping("/admin/recommend")
    public ResponseEntity<Place> updatePlace(@RequestBody AdminRecommendUpdatePlaceDto updatePlaceRequestDto) {
        Place updatedPlace = service.updatePlace(updatePlaceRequestDto);
        return ResponseEntity.ok(updatedPlace);
    }

    @DeleteMapping("/admin/recommend")
    public ResponseEntity<Long> deletePlace(@RequestParam long placeId) {
        Long deletedPlaceId = service.deletePlace(placeId);
        return ResponseEntity.ok(deletedPlaceId);
    }
}
