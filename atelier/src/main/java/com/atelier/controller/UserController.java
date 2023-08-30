package com.atelier.controller;

import com.atelier.dto.AuthenticationRequestDTO;
import com.atelier.dto.AuthenticationResponseDTO;
import com.atelier.dto.RegisterDTO;
import com.atelier.dto.UserDTO;
import com.atelier.repository.UserRepo;
import com.atelier.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/user")
public class UserController {

    private final AuthenticationService authenticationService;
    private final UserRepo userRepo;

    @PostMapping("/register")
    public AuthenticationResponseDTO register(@RequestBody RegisterDTO request) {
        return authenticationService.register(request);
    }

    @PostMapping("/login")
    public AuthenticationResponseDTO login(@RequestBody AuthenticationRequestDTO request) {
        return authenticationService.login(request);
    }

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userRepo.findAll().stream().map(user -> new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getOrders())).collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        try {
            userRepo.delete(userRepo.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found")));
            return ResponseEntity.ok("user delete Successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
