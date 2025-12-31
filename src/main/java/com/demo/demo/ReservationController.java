package com.demo.demo;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/reservation")
public class ReservationController {

    private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/add")
    public ResponseEntity<Reservation> createReservation(
            @RequestBody Reservation reservation
    ) {
        logger.info("Started creating reservation");
        var saved_reservation = reservationService.createReservation(reservation);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(saved_reservation);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Reservation>> getALlReservationById(
    ) {
        logger.info("GET /reservation/all");
        return ResponseEntity.ok(reservationService.getAllReservations());
    }

    @PutMapping("/update")
    public ResponseEntity<Reservation> updateReservation(
            @RequestBody Reservation reservation
    ) {
        logger.info("Updating reservation #{}", reservation.id());
        return ResponseEntity.ok(reservationService.updateReservation(reservation));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Reservation> deleteReservationById(
            @PathVariable("id") Long id
    ) {
        reservationService.deleteReservation(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(
            @PathVariable("id") Long id
    ) {
        logger.info("GET /reservation/{}", id);
        return ResponseEntity.ok(reservationService.getReservationById(id));
    }

    @PostMapping("/{id}/accept")
    public ResponseEntity<Reservation> approveReservationById(
            @PathVariable("id") Long id
    ) {
        logger.info("POST /reservation/{}/approve", id);
        return ResponseEntity.ok(reservationService.approveReservationById(id));
    }

}
