package com.example.Triple_clone.controller.recommend.manager;

import com.example.Triple_clone.dto.recommend.manager.RecommendForManagerCreatePlaceRequestDto;
import com.example.Triple_clone.dto.recommend.manager.RecommendForManagerUpdatePlaceRequestDto;
import com.example.Triple_clone.service.recommend.manager.RecommendForManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class RecommendForManagerController {
    private final RecommendForManagerService service;

    @PostMapping("/recommend/manager/create")
    public ResponseEntity<Void> createPlace(@RequestBody RecommendForManagerCreatePlaceRequestDto createPlaceRequestDto) {
        service.createPlace(createPlaceRequestDto);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/recommend/manager/update")
    public ResponseEntity<Void> updatePlace(@RequestBody RecommendForManagerUpdatePlaceRequestDto updatePlaceRequestDto) {
        service.updatePlace(updatePlaceRequestDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/recommend/manager/delete")
    public ResponseEntity<Void> deletePlace(@RequestParam Long placeId) {
        service.deletePlace(placeId);
        return ResponseEntity.noContent().build();
    }
}
