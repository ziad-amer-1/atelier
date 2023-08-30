package com.atelier.controller;

import com.atelier.dto.CreateAppointmentDTO;
import com.atelier.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
@RequestMapping("/api/appointment")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @PostMapping
    public ResponseEntity<String> createNewAppointment(@RequestHeader("Authorization") String token, @RequestBody CreateAppointmentDTO createAppointmentDTO) {
        try {
            return ResponseEntity.ok(appointmentService.createAppointment(createAppointmentDTO.withToken(token)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllAppointmentsOnSpecificDate(@RequestParam(value = "date", required = false) String date) {
        try {
            if (date == null) {
                date = LocalDate.now().format(formatter);
                log.info("current date: " + date);
            }
            return ResponseEntity.ok(appointmentService.getAllAppointmentsOnSpecificDate(date));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/get-booked-times")
    public ResponseEntity<?> bookedTimesOnSpecificDate(@RequestParam(value = "date", required = false) String date) {
        try {
            if (date == null) {
                date = LocalDate.now().format(formatter);
                log.info("current date: " + date);
            }
            return ResponseEntity.ok(appointmentService.bookedTimesOnSpecificDate(date));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
