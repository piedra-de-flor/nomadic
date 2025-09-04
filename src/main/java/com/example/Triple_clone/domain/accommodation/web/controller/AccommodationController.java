package com.example.Triple_clone.domain.accommodation.web.controller;

import com.example.Triple_clone.domain.accommodation.application.AccommodationCommandService;
import com.example.Triple_clone.domain.accommodation.application.AccommodationQueryService;
import com.example.Triple_clone.domain.accommodation.domain.AccommodationDocument;
import com.example.Triple_clone.domain.accommodation.domain.SortOption;
import com.example.Triple_clone.domain.accommodation.web.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(summary = "숙소 검색 - 자동완성", description = "숙소를 검색시 자동완성 기능을 제공합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @ApiResponse(responseCode = "401", description = "권한 인증 오류 발생")
    @GetMapping("/autocomplete")
    public ResponseEntity<List<AutocompleteResult>> smartAutocomplete(
            @RequestParam String q,
            @RequestParam(defaultValue = "10") int limit
    ) {
        List<AutocompleteResult> results = accommodationQueryService.getSmartAutocomplete(q, limit);
        return ResponseEntity.ok(results);
    }

    @Operation(summary = "숙소 검색 - 오타 검즘", description = "숙소를 검색시 오타 겅증 기능을 제공합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @ApiResponse(responseCode = "401", description = "권한 인증 오류 발생")
    @GetMapping("/typo-correction")
    public ResponseEntity<SpellCheckResponse> checkSpelling(
            @RequestParam String query
    ) {
        SpellCheckResponse response = accommodationQueryService.checkSpelling(query);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "숙소 검색", description = "숙소를 검색합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @ApiResponse(responseCode = "401", description = "권한 인증 오류 발생")
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

    @Operation(summary = "숙소 데이터 생성", description = "새로운 숙소 데이터를 생성합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @ApiResponse(responseCode = "401", description = "권한 인증 오류 발생")
    @PostMapping
    public ResponseEntity<AccommodationDto> createAccommodation(
            @Valid @RequestBody AccommodationCreateDto dto
    ) {
        AccommodationDto created = accommodationCommandService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "객실 추가", description = "숙소에 새로운 객실을 추가합니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @ApiResponse(responseCode = "401", description = "권한 인증 오류 발생")
    @PostMapping("/{accommodationId}/rooms")
    public ResponseEntity<RoomAddDto> addRoom(
            @PathVariable Long accommodationId,
            @Valid @RequestBody RoomAddDto roomDto
    ) {
        accommodationCommandService.addRoom(accommodationId, roomDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(roomDto);
    }

    @Operation(summary = "객실 수정", description = "객실 정보를 수정합니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @ApiResponse(responseCode = "401", description = "권한 인증 오류 발생")
    @PutMapping("/{accommodationId}/rooms/{roomId}")
    public ResponseEntity<AccommodationDto> updateRoom(
            @PathVariable Long accommodationId,
            @PathVariable Long roomId,
            @Valid @RequestBody RoomUpdateDto roomDto
    ) {
        accommodationCommandService.updateRoom(accommodationId, roomId, roomDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "객실 삭제", description = "특정 객실을 삭제합니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @ApiResponse(responseCode = "401", description = "권한 인증 오류 발생")
    @DeleteMapping("/{accommodationId}/rooms/{roomId}")
    public ResponseEntity<Void> removeRoom(
            @PathVariable Long accommodationId,
            @PathVariable Long roomId
    ) {
        accommodationCommandService.removeRoom(accommodationId, roomId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "숙소 삭제", description = "특정 숙소를 삭제합니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @ApiResponse(responseCode = "401", description = "권한 인증 오류 발생")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccommodation(@PathVariable Long id) {
        accommodationCommandService.delete(id);
        return ResponseEntity.ok().build();
    }
}