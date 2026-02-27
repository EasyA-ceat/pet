package com.pet.management.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pet.management.model.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long>, JpaSpecificationExecutor<Appointment> {

    List<Appointment> findByCustomerId(Long customerId);

    List<Appointment> findByStatus(String status);

    @Query("SELECT a FROM Appointment a WHERE a.appointmentTime BETWEEN :start AND :end ORDER BY a.appointmentTime")
    List<Appointment> findByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT a FROM Appointment a WHERE DATE(a.appointmentTime) = CURRENT_DATE ORDER BY a.appointmentTime")
    List<Appointment> findTodayAppointments();

    @Query("SELECT a FROM Appointment a WHERE a.appointmentTime >= :now ORDER BY a.appointmentTime")
    List<Appointment> findUpcomingAppointments(@Param("now") LocalDateTime now);
}
