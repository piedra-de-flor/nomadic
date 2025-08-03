package com.example.Triple_clone.domain.accommodation.web.controller;

import com.example.Triple_clone.domain.accommodation.application.AccommodationQueryService;
import com.example.Triple_clone.domain.accommodation.domain.SortOption;
import com.example.Triple_clone.domain.accommodation.web.dto.AccommodationDto;
import com.example.Triple_clone.domain.accommodation.web.dto.RoomDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/accommodations")
public class AccommodationController {
    private final AccommodationQueryService service;

    @GetMapping("/search")
    public ResponseEntity<List<AccommodationDto>> searchAccommodations(
            @RequestParam(required = false) String searchKeyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Float ratingMin,
            @RequestParam(required = false) Float ratingMax,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) Integer dayusePriceMin,
            @RequestParam(required = false) Integer dayusePriceMax,
            @RequestParam(required = false) Boolean dayuseAvailable,
            @RequestParam(required = false) Boolean hasDayuseDiscount,
            @RequestParam(required = false) Integer stayPriceMin,
            @RequestParam(required = false) Integer stayPriceMax,
            @RequestParam(required = false) Boolean stayAvailable,
            @RequestParam(required = false) Boolean hasStayDiscount,
            @RequestParam(required = false) Integer roomPriceMin,
            @RequestParam(required = false) Integer roomPriceMax,
            @RequestParam(required = false) Integer roomCapacityMin,
            @RequestParam(required = false) Integer roomCapacityMax,
            @RequestParam(required = false) String roomCheckoutTime,
            @RequestParam(required = false) SortOption sortOption,
            Pageable pageable
    ) {
        List<AccommodationDto> result = service.searchES(
                searchKeyword, category,
                ratingMin, ratingMax,
                region,
                dayusePriceMin, dayusePriceMax,
                dayuseAvailable,
                hasDayuseDiscount,
                stayPriceMin, stayPriceMax,
                stayAvailable,
                hasStayDiscount,
                roomPriceMin, roomPriceMax,
                roomCapacityMin, roomCapacityMax,
                roomCheckoutTime,
                sortOption,
                pageable
        );
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}/rooms")
    public ResponseEntity<List<RoomDto>> getRoomsByAccommodationId(@PathVariable long id) {
        return ResponseEntity.ok(service.findRoomsByAccommodationId(id));
    }
}
