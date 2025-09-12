package com.example.Triple_clone.domain.accommodation.web.controller;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import com.example.Triple_clone.domain.accommodation.domain.Accommodation;
import com.example.Triple_clone.domain.accommodation.domain.AccommodationDocument;
import com.example.Triple_clone.domain.accommodation.domain.Room;
import com.example.Triple_clone.domain.accommodation.domain.RoomDocument;
import com.example.Triple_clone.domain.accommodation.infra.AccommodationRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class DataMigrationController {

    private final AccommodationRepository accommodationRepository;
    private final ElasticsearchClient elasticsearchClient;

    @Operation(summary = "객실 데이터를 migration합니다 (MySQL -> ES)", description = "필요시 한번만 호출됩니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @ApiResponse(responseCode = "401", description = "권한 인증 오류 발생")
    @PostMapping("/migrate-to-es")
    public ResponseEntity<String> migrateToES() {
        try {
            log.info("ES 데이터 마이그레이션 시작");

            List<Accommodation> accommodations = accommodationRepository.findAllWithRooms();
            log.info("MySQL에서 {} 건의 숙소 데이터를 조회했습니다", accommodations.size());

            int totalRooms = accommodations.stream()
                    .mapToInt(acc -> acc.getRooms().size())
                    .sum();
            log.info("총 {} 개의 방 데이터가 포함되어 있습니다", totalRooms);

            bulkInsertToES(accommodations);

            log.info("ES 데이터 마이그레이션 완료: 숙소 {} 건, 방 {} 개", accommodations.size(), totalRooms);
            return ResponseEntity.ok(String.format("마이그레이션 완료: 숙소 %d 건, 방 %d 개", accommodations.size(), totalRooms));

        } catch (Exception e) {
            log.error("마이그레이션 실패", e);
            return ResponseEntity.status(500).body("마이그레이션 실패: " + e.getMessage());
        }
    }

    private void bulkInsertToES(List<Accommodation> accommodations) throws IOException {
        BulkRequest.Builder bulkBuilder = new BulkRequest.Builder();

        for (Accommodation acc : accommodations) {
            AccommodationDocument doc = convertToDocument(acc);

            bulkBuilder.operations(op -> op
                    .index(idx -> idx
                            .index("accommodation")
                            .id(String.valueOf(doc.getId()))
                            .document(doc)
                    )
            );
        }

        BulkResponse response = elasticsearchClient.bulk(bulkBuilder.build());

        if (response.errors()) {
            log.error("벌크 삽입 중 일부 오류 발생");
            response.items().forEach(item -> {
                if (item.error() != null) {
                    log.error("오류 항목 ID {}: {}", item.id(), item.error().reason());
                }
            });
        } else {
            log.info("모든 데이터가 성공적으로 ES에 삽입되었습니다");
        }
    }

    private AccommodationDocument convertToDocument(Accommodation accommodation) {
        List<RoomDocument> roomDocuments = null;
        RoomDocument previewRoom = null;
        Integer minStayPrice = null;

        if (accommodation.getRooms() != null && !accommodation.getRooms().isEmpty()) {
            roomDocuments = accommodation.getRooms().stream()
                    .map(this::convertToRoomDocument)
                    .collect(Collectors.toList());

            previewRoom = roomDocuments.get(0);

            minStayPrice = accommodation.getRooms().stream()
                    .filter(room -> room.getStayPrice() != null && room.getStayPrice() > 0)
                    .mapToInt(Room::getStayPrice)
                    .min()
                    .orElse(0);
        }

        return AccommodationDocument.builder()
                .id(accommodation.getId())
                .image(accommodation.getImage())
                .name(accommodation.getName())
                .category(accommodation.getCategory())
                .grade(accommodation.getGrade())
                .rating(accommodation.getRating())
                .reviewCount(accommodation.getReviewCount())
                .region(accommodation.getRegion())
                .address(accommodation.getAddress())
                .landmarkDistance(accommodation.getLandmarkDistance())
                .intro(accommodation.getIntro())
                .amenities(accommodation.getAmenities())
                .info(accommodation.getInfo())
                .minStayPrice(minStayPrice)
                .previewRoom(previewRoom)
                .rooms(roomDocuments)
                .build();
    }

    private RoomDocument convertToRoomDocument(Room room) {
        return RoomDocument.builder()
                .id(room.getId())
                .name(room.getName())
                .dayusePrice(room.getDayusePrice())
                .dayuseSalePrice(room.getDayuseSalePrice())
                .hasDayuseDiscount(room.getHasDayuseDiscount())
                .dayuseSoldout(room.getDayuseSoldout())
                .dayuseTime(room.getDayuseTime())
                .stayPrice(room.getStayPrice())
                .staySalePrice(room.getStaySalePrice())
                .hasStayDiscount(room.getHasStayDiscount())
                .staySoldout(room.getStaySoldout())
                .stayCheckinTime(room.getStayCheckinTime())
                .stayCheckoutTime(room.getStayCheckoutTime())
                .capacity(room.getCapacity())
                .maxCapacity(room.getMaxCapacity())
                .build();
    }

    @Operation(summary = "ES status 확인", description = "ES의 상태를 확입합니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @ApiResponse(responseCode = "401", description = "권한 인증 오류 발생")
    @GetMapping("/es-status")
    public ResponseEntity<Map<String, Object>> getESStatus() {
        try {
            var countResponse = elasticsearchClient.count(c -> c.index("accommodation"));

            Map<String, Object> status = new HashMap<>();
            status.put("totalDocuments", countResponse.count());
            status.put("indexExists", true);

            return ResponseEntity.ok(status);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            error.put("indexExists", false);
            return ResponseEntity.status(500).body(error);
        }
    }
}