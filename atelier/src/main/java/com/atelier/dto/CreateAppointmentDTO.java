package com.atelier.dto;

import java.time.LocalDate;

public record CreateAppointmentDTO(
    String token,
    LocalDate date,
    String time,
    Long orderId
) {
    public CreateAppointmentDTO withToken(String token) {
        return new CreateAppointmentDTO(token, date(), time(), orderId());
    }
}
