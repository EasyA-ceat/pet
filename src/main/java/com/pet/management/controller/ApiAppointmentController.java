package com.pet.management.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pet.management.model.Appointment;
import com.pet.management.model.Customer;
import com.pet.management.service.AppointmentService;
import com.pet.management.service.CustomerService;

@RestController
@RequestMapping("/api/appointments")
public class ApiAppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public List<Appointment> getAllAppointments() {
        return appointmentService.findAll();
    }

    @GetMapping("/{id}")
    public Appointment getAppointmentById(@PathVariable Long id) {
        return appointmentService.findById(id).orElse(null);
    }

    @GetMapping("/customer/{customerId}")
    public List<Appointment> getAppointmentsByCustomerId(@PathVariable Long customerId) {
        return appointmentService.findByCustomerId(customerId);
    }

    @GetMapping("/status/{status}")
    public List<Appointment> getAppointmentsByStatus(@PathVariable String status) {
        return appointmentService.findByStatus(status);
    }

    @GetMapping("/today")
    public List<Appointment> getTodayAppointments() {
        return appointmentService.findTodayAppointments();
    }

    @GetMapping("/upcoming")
    public List<Appointment> getUpcomingAppointments() {
        return appointmentService.findUpcomingAppointments();
    }

    @GetMapping("/date-range")
    public List<Appointment> getAppointmentsByDateRange(
            @RequestParam String start,
            @RequestParam String end) {
        LocalDateTime startDate = LocalDateTime.parse(start);
        LocalDateTime endDate = LocalDateTime.parse(end);
        return appointmentService.findByDateRange(startDate, endDate);
    }

    @PostMapping
    public Appointment createAppointment(@RequestBody Map<String, Object> requestBody) {
        Long customerId = Long.valueOf(requestBody.get("customerId").toString());
        Customer customer = customerService.findById(customerId).orElse(null);
        if (customer == null) {
            return null;
        }

        Appointment appointment = new Appointment();
        appointment.setCustomer(customer);
        appointment.setAppointmentTime(LocalDateTime.parse(requestBody.get("appointmentTime").toString()));
        appointment.setServiceType(requestBody.get("serviceType").toString());
        
        if (requestBody.containsKey("endTime")) {
            appointment.setEndTime(LocalDateTime.parse(requestBody.get("endTime").toString()));
        }
        if (requestBody.containsKey("status")) {
            appointment.setStatus(requestBody.get("status").toString());
        }
        if (requestBody.containsKey("notes")) {
            appointment.setNotes(requestBody.get("notes").toString());
        }

        return appointmentService.save(appointment);
    }

    @PutMapping("/{id}")
    public Appointment updateAppointment(@PathVariable Long id, @RequestBody Map<String, Object> requestBody) {
        Appointment existingAppointment = appointmentService.findById(id).orElse(null);
        if (existingAppointment == null) {
            return null;
        }

        if (requestBody.containsKey("customerId")) {
            Long customerId = Long.valueOf(requestBody.get("customerId").toString());
            Customer customer = customerService.findById(customerId).orElse(null);
            if (customer != null) {
                existingAppointment.setCustomer(customer);
            }
        }

        if (requestBody.containsKey("appointmentTime")) {
            existingAppointment.setAppointmentTime(LocalDateTime.parse(requestBody.get("appointmentTime").toString()));
        }
        if (requestBody.containsKey("endTime")) {
            existingAppointment.setEndTime(LocalDateTime.parse(requestBody.get("endTime").toString()));
        }
        if (requestBody.containsKey("serviceType")) {
            existingAppointment.setServiceType(requestBody.get("serviceType").toString());
        }
        if (requestBody.containsKey("status")) {
            existingAppointment.setStatus(requestBody.get("status").toString());
        }
        if (requestBody.containsKey("notes")) {
            existingAppointment.setNotes(requestBody.get("notes").toString());
        }

        return appointmentService.save(existingAppointment);
    }

    @DeleteMapping("/{id}")
    public void deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteById(id);
    }

    // 统计信息
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getAppointmentStats() {
        List<Appointment> allAppointments = appointmentService.findAll();
        Map<String, Object> stats = new HashMap<>();

        // 总预约数
        stats.put("totalAppointments", allAppointments.size());

        // 各状态的统计
        int pendingCount = 0, confirmedCount = 0, completedCount = 0, cancelledCount = 0;
        for (Appointment app : allAppointments) {
            if ("PENDING".equals(app.getStatus())) {
                pendingCount++;
            } else if ("CONFIRMED".equals(app.getStatus())) {
                confirmedCount++;
            } else if ("COMPLETED".equals(app.getStatus())) {
                completedCount++;
            } else if ("CANCELLED".equals(app.getStatus())) {
                cancelledCount++;
            }
        }

        stats.put("pendingCount", pendingCount);
        stats.put("confirmedCount", confirmedCount);
        stats.put("completedCount", completedCount);
        stats.put("cancelledCount", cancelledCount);

        // 今日预约
        List<Appointment> todayAppointments = appointmentService.findTodayAppointments();
        stats.put("todayCount", todayAppointments.size());

        return ResponseEntity.ok(stats);
    }
}
