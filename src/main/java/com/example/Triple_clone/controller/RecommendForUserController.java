package com.example.Triple_clone.controller;

import com.example.Triple_clone.dto.RecommendForUserLikeRequestDto;
import com.example.Triple_clone.dto.RecommendForUserReadAllResponseDto;
import com.example.Triple_clone.dto.RecommendForUserReadResponseDto;
import com.example.Triple_clone.service.RecommendForUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RecommendForUserController {
    private final RecommendForUserService service;

    RecommendForUserController(RecommendForUserService service) {
        this.service = service;
    }

    @GetMapping("/recommend/user/all")
    public ResponseEntity<RecommendForUserReadAllResponseDto> readAll() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/recommend/user/{placeId}")
    public ResponseEntity<RecommendForUserReadResponseDto> read(@PathVariable long placeId, @RequestParam long userId) {
        RecommendForUserReadResponseDto recommendForUserReadResponseDto = service.findById(placeId, userId);
        return ResponseEntity.ok(recommendForUserReadResponseDto);
    }

    @PutMapping("/recommend/user/like")
    public ResponseEntity<Void> like(@RequestBody RecommendForUserLikeRequestDto recommendForUserLikeRequestDto) {
        service.like(recommendForUserLikeRequestDto.getPlaceId(), recommendForUserLikeRequestDto.getUserId());
        return ResponseEntity.noContent().build();
    }
}
