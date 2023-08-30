package com.atelier.controller;

import com.atelier.dto.ChangePasswordDTO;
import com.atelier.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
@RequestMapping("/api/user-profile")
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping("/user-info")
    public ResponseEntity<?> getUserInfo(@RequestHeader("Authorization") String token) {
        try {
            return ResponseEntity.ok(userProfileService.getUserInfo(token));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PatchMapping("/update-user-password")
    public ResponseEntity<?> updateUserPassword(@RequestHeader("Authorization") String token, @RequestBody ChangePasswordDTO changePasswordDTO
            ) {
        try {
            return ResponseEntity.ok(userProfileService.updateUserPassword(token, changePasswordDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
