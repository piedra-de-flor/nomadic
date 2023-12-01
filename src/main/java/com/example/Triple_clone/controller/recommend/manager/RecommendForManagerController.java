package com.example.Triple_clone.controller.recommend.manager;

import com.example.Triple_clone.dto.recommend.manager.RecommendForManagerCreatePlaceRequestDto;
import com.example.Triple_clone.service.recommend.manager.RecommendForManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RecommendForManagerController {
    private final RecommendForManagerService service;

    @PostMapping("/recommend/manager/create")
    public ResponseEntity<Void> createPlace(@RequestBody RecommendForManagerCreatePlaceRequestDto createPlaceRequestDto) {
        service.createPlace(createPlaceRequestDto);
        return ResponseEntity.noContent().build();
    }
}
