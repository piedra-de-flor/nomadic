package com.example.Triple_clone.web.controller.reservation;

import com.example.Triple_clone.dto.planning.DetailPlanDto;
import com.example.Triple_clone.service.planning.ReservationFacadeService;
import com.example.Triple_clone.service.support.FileReader;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationFacadeService reservationFacadeService;
    private final FileReader fileReader;

    @GetMapping("/reservations")
    public ResponseEntity<List<String>> readAllOrderBy(@RequestParam String local
                                                 /*@RequestParam(required = false, defaultValue = "") String sort,
                                                 @RequestParam long page*/) {
        return ResponseEntity.ok(fileReader.findAccommodations(local/*, sort, page*/));
    }

    @GetMapping("/myReservations")
    public ResponseEntity<List<DetailPlanDto>> findAllReservations(@RequestParam long userId) {
        List<DetailPlanDto> response = reservationFacadeService.findAllReservation(userId);
        return ResponseEntity.ok(response);
    }
}
