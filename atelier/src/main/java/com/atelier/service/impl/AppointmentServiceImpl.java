package com.atelier.service.impl;

import com.atelier.dto.CreateAppointmentDTO;
import com.atelier.entity.AppUser;
import com.atelier.entity.Appointment;
import com.atelier.entity.UserOrder;
import com.atelier.repository.AppointmentRepo;
import com.atelier.repository.OrderRepo;
import com.atelier.repository.UserRepo;
import com.atelier.service.AppointmentService;
import com.atelier.utils.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepo appointmentRepo;
    private final JwtTokenProvider tokenProvider;
    private final UserRepo userRepo;
    private final OrderRepo orderRepo;

    @Override
    public String createAppointment(CreateAppointmentDTO createAppointmentDTO) {

        Claims tokenClaims = tokenProvider.extractClaims(createAppointmentDTO.token().split(" ")[1]);
        AppUser user = userRepo.findById(Long.valueOf((String) tokenClaims.get("userId"))).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Appointment appointment = new Appointment();
        appointment.setDate(createAppointmentDTO.date());
        appointment.setTime(createAppointmentDTO.time());
        appointment.setUser(user);
        appointment.setOrder(orderRepo.findById(createAppointmentDTO.orderId()).orElseThrow(() -> new IllegalStateException("Order with id " + createAppointmentDTO.orderId() + " not exist")));

        appointmentRepo.save(appointment);

        return "New Appointment {" + appointment.getDate() + ", " + appointment.getTime() + "} created to user: " + user.getUsername();
    }

    @Override
    @Cacheable("appointments")
    public List<Appointment> getAllAppointmentsOnSpecificDate(String date) {
        return appointmentRepo.getAllAppointmentsOnSpecificDate(date);
    }

    @Override
    public List<String> bookedTimesOnSpecificDate(String date) {
        return appointmentRepo.bookedTimesOnSpecificDate(date);
    }
}
