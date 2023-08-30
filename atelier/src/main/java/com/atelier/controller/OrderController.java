package com.atelier.controller;

import com.atelier.dto.CreateOrderDTO;
import com.atelier.service.OrderService;
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
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<String> createNewOrder(@RequestHeader("Authorization") String token, @RequestBody CreateOrderDTO createOrderDTO) {
        try {
             return ResponseEntity.ok(orderService.createOrder(createOrderDTO.withToken(token)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllOrdersInSpecificDate(@RequestParam(value = "date", required = false) String date, @RequestParam(value = "status", required = false) String status) {
        try {
            if (date == null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                date = LocalDate.now().format(formatter);
                log.info("current date: " + date);
            }
            return ResponseEntity.ok(orderService.getAllOrdersInSpecificDate(date, status));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PatchMapping("/change-status/{id}")
    public ResponseEntity<?> changeOrderStatus(@PathVariable Long id, @RequestParam("status") String status) {
        try {
            return ResponseEntity.ok(orderService.changeOrderStatus(id, status));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


}
