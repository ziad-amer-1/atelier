package com.atelier.repository;

import com.atelier.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AppointmentRepo extends JpaRepository<Appointment, Long> {
    @Query("""
        SELECT a FROM Appointment a WHERE (?1 IS NULL OR to_char(a.date, 'yyyy-MM-dd') = ?1)
    """)
    List<Appointment> getAllAppointmentsOnSpecificDate(String date);

    @Query("""
        SELECT a.time
        FROM Appointment a
        WHERE to_char(a.date, 'yyyy-MM-dd') = ?1
        GROUP BY a.time
        HAVING COUNT(a.id) > 2
    """)
    List<String> bookedTimesOnSpecificDate(String date);
}
