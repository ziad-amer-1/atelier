package com.atelier.repository;

import com.atelier.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepo extends JpaRepository<Long, Appointment> {
}
