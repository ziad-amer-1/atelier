package com.atelier.controller;

import com.atelier.dto.CreateOrderDTO;
import com.atelier.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
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
    public ResponseEntity<?> getAllOrdersInSpecificDate(@RequestParam(value = "date", required = false) String date) {
        try {
            return ResponseEntity.ok(orderService.getAllOrdersInSpecificDate(date));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
