package com.example.Triple_clone.controller.recommend.manager;

import com.example.Triple_clone.dto.recommend.manager.AdminRecommendCreatePlaceDto;
import com.example.Triple_clone.dto.recommend.manager.AdminRecommendUpdatePlaceDto;
import com.example.Triple_clone.service.recommend.manager.AdminRecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AdminRecommendController {
    private final AdminRecommendService service;

    @PostMapping("/recommend/manager/create")
    public ResponseEntity<Void> createPlace(@RequestBody AdminRecommendCreatePlaceDto createPlaceRequestDto) {
        service.createPlace(createPlaceRequestDto);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/recommend/manager/update")
    public ResponseEntity<Void> updatePlace(@RequestBody AdminRecommendUpdatePlaceDto updatePlaceRequestDto) {
        service.updatePlace(updatePlaceRequestDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/recommend/manager/delete")
    public ResponseEntity<Void> deletePlace(@RequestParam Long placeId) {
        service.deletePlace(placeId);
        return ResponseEntity.noContent().build();
    }
}
