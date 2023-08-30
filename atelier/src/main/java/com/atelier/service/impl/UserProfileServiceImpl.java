package com.atelier.service.impl;

import com.atelier.dto.ChangePasswordDTO;
import com.atelier.dto.UserDTO;
import com.atelier.entity.AppUser;
import com.atelier.repository.UserRepo;
import com.atelier.service.UserProfileService;
import com.atelier.utils.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserProfileServiceImpl implements UserProfileService {

    private final UserRepo userRepo;
    private final JwtTokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Cacheable("user-info")
    public UserDTO getUserInfo(String token) {
        Claims tokenClaims = tokenProvider.extractClaims(token.split(" ")[1]);
        AppUser user = userRepo.findById(Long.valueOf((String) tokenClaims.get("userId"))).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getOrders());
    }

    @Override
    @Transactional
    public String updateUserPassword(String token, ChangePasswordDTO changePasswordDTO) {
        Claims tokenClaims = tokenProvider.extractClaims(token.split(" ")[1]);
        AppUser user =  userRepo.findById(Long.valueOf((String) tokenClaims.get("userId"))).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (passwordEncoder.matches(changePasswordDTO.oldPass(), user.getPassword())) {
            log.info("old password input is equal to user password");
            user.setPassword(passwordEncoder.encode(changePasswordDTO.newPass()));
        } else {
            log.error("old password input is not equal to user password");
            throw new IllegalStateException("old password input is not equal to user password");
        }
        return "password updated Successfully";
    }
}
