package com.atelier.service;

import com.atelier.dto.AuthenticationRequestDTO;
import com.atelier.dto.AuthenticationResponseDTO;
import com.atelier.dto.RegisterDTO;

public interface AuthenticationService {
    AuthenticationResponseDTO register(RegisterDTO registerRequest);
    AuthenticationResponseDTO login(AuthenticationRequestDTO authenticationRequest);
}
