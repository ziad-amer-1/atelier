package com.atelier.dto;

import lombok.Builder;

@Builder
public record ChangePasswordDTO(
    String oldPass,
    String newPass
) {
}
