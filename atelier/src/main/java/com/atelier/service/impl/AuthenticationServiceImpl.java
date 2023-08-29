package com.atelier.service.impl;

import com.atelier.dto.AuthenticationRequestDTO;
import com.atelier.dto.AuthenticationResponseDTO;
import com.atelier.dto.RegisterDTO;
import com.atelier.entity.AppUser;
import com.atelier.repository.UserRepo;
import com.atelier.service.AuthenticationService;
import com.atelier.utils.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public AuthenticationResponseDTO register(RegisterDTO registerRequest) {

        if (registerRequest.username() == null || registerRequest.email() == null || registerRequest.password() == null) {
            throw new IllegalStateException("you should provide all info [username, email, password]");
        }

        boolean isUserAlreadyExist = userRepo.findByUsername(registerRequest.username()).isPresent();

        if (isUserAlreadyExist) {
            throw new IllegalStateException("This User Already Exist");
        }
        AppUser user = AppUser
                .builder()
                .username(registerRequest.username().trim())
                .email(registerRequest.email().trim())
                .password(passwordEncoder.encode(registerRequest.password()))
                .build();

        userRepo.save(user);

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("userId", userRepo.findByUsername(user.getUsername()).get().getId().toString());
        userInfo.put("username", user.getUsername().trim());

        String jwt = jwtTokenProvider.generateToken(userInfo, user);

        return AuthenticationResponseDTO
                .builder()
                .token(jwt)
                .build();

    }

    @Override
    public AuthenticationResponseDTO login(AuthenticationRequestDTO authenticationRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getUsername().trim(),
                            authenticationRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException bc) {
            log.error("Wrong Username or Password");
            throw new IllegalStateException("Wrong Username or Password");
        }

        AppUser appUser = userRepo.findByUsername(authenticationRequest.getUsername().trim()).orElseThrow(() -> new UsernameNotFoundException("Username or Password is Wrong"));
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("userId", userRepo.findByUsername(appUser.getUsername()).get().getId().toString());
        userInfo.put("username", appUser.getUsername());

        String jwtToken = jwtTokenProvider.generateToken(userInfo, appUser);


        return AuthenticationResponseDTO
                .builder()
                .token(jwtToken)
                .build();

    }
}
