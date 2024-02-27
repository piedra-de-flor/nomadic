package com.example.Triple_clone.web.controller.reservation;

import com.example.Triple_clone.dto.planning.DetailPlanDto;
import com.example.Triple_clone.service.planning.ReservationService;
import com.example.Triple_clone.service.support.FileManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @GetMapping("/reservations")
    public ResponseEntity<Map<String, Long>> readAll(@RequestParam String local) {
        return ResponseEntity.ok(reservationService.findAllAccommodations(local));
    }

    @GetMapping("/reservations/price")
    public ResponseEntity<Map<String, Long>> readAllOrderByPrice(@RequestParam String local) {
        return ResponseEntity.ok(reservationService.findAllAccommodationsSortByPrice(local));
    }

    @GetMapping("/myReservations")
    public ResponseEntity<List<DetailPlanDto>> findAllMyReservations(@RequestParam long userId) {
        List<DetailPlanDto> response = reservationService.findAllMyReservation(userId);
        return ResponseEntity.ok(response);
    }
}
