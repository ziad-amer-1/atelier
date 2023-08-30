package com.atelier.service;

import com.atelier.dto.CreateAppointmentDTO;
import com.atelier.entity.Appointment;

import java.util.List;

public interface AppointmentService {

    String createAppointment(CreateAppointmentDTO createAppointmentDTO);
    List<Appointment> getAllAppointmentsOnSpecificDate(String date);

    List<String> bookedTimesOnSpecificDate(String date);

}
