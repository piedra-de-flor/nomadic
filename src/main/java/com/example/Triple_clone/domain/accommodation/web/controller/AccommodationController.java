package com.example.Triple_clone.domain.accommodation.web.controller;

import com.example.Triple_clone.domain.accommodation.application.AccommodationCommandService;
import com.example.Triple_clone.domain.accommodation.application.AccommodationQueryService;
import com.example.Triple_clone.domain.accommodation.domain.AccommodationDocument;
import com.example.Triple_clone.domain.accommodation.domain.SortOption;
import com.example.Triple_clone.domain.accommodation.web.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accommodations")
@RequiredArgsConstructor
public class AccommodationController {
    private final AccommodationCommandService accommodationCommandService;
    private final AccommodationQueryService accommodationQueryService;

    @GetMapping("/autocomplete")
    public ResponseEntity<List<AutocompleteResult>> smartAutocomplete(
            @RequestParam String q,
            @RequestParam(defaultValue = "10") int limit
    ) {
        List<AutocompleteResult> results = accommodationQueryService.getSmartAutocomplete(q, limit);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/typo-correction")
    public ResponseEntity<SpellCheckResponse> checkSpelling(
            @RequestParam String query
    ) {
        SpellCheckResponse response = accommodationQueryService.checkSpelling(query);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<AccommodationDocument>> searchAccommodations(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "ID_ASC") SortOption sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        List<AccommodationDocument> results = accommodationQueryService.searchAccommodations(q, sort, page, size);
        return ResponseEntity.ok(results);
    }

    @PostMapping
    public ResponseEntity<AccommodationDto> createAccommodation(
            @Valid @RequestBody AccommodationCreateDto dto
    ) {
        AccommodationDto created = accommodationCommandService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/{accommodationId}/rooms")
    public ResponseEntity<RoomAddDto> addRoom(
            @PathVariable Long accommodationId,
            @Valid @RequestBody RoomAddDto roomDto
    ) {
        accommodationCommandService.addRoom(accommodationId, roomDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(roomDto);
    }

    @PutMapping("/{accommodationId}/rooms/{roomId}")
    public ResponseEntity<AccommodationDto> updateRoom(
            @PathVariable Long accommodationId,
            @PathVariable Long roomId,
            @Valid @RequestBody RoomUpdateDto roomDto
    ) {
        accommodationCommandService.updateRoom(accommodationId, roomId, roomDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{accommodationId}/rooms/{roomId}")
    public ResponseEntity<Void> removeRoom(
            @PathVariable Long accommodationId,
            @PathVariable Long roomId
    ) {
        accommodationCommandService.removeRoom(accommodationId, roomId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccommodation(@PathVariable Long id) {
        accommodationCommandService.delete(id);
        return ResponseEntity.ok().build();
    }
}