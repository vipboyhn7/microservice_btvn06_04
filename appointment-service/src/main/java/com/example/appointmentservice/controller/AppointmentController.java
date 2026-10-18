package com.example.appointmentservice.controller;

import com.example.appointmentservice.entity.Appointment;
import com.example.appointmentservice.repository.AppointmentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentController {

    private final AppointmentRepository appointmentRepository;
    private final RestTemplate restTemplate;

    public AppointmentController(AppointmentRepository appointmentRepository, RestTemplate restTemplate) {
        this.appointmentRepository = appointmentRepository;
        this.restTemplate = restTemplate;
    }

    @PostMapping
    public ResponseEntity<?> createAppointment(@RequestBody Appointment appointment) {
        if (!exists("patient-service", appointment.getPatientId())
                || !exists("doctor-service", appointment.getDoctorId())) {
            return ResponseEntity.badRequest()
                    .body("Patient or doctor does not exist");
        }

        if (appointment.getStatus() == null || appointment.getStatus().isBlank()) {
            appointment.setStatus("PENDING");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(appointmentRepository.save(appointment));
    }

    private boolean exists(String serviceName, Long id) {
        if (id == null) {
            return false;
        }
        try {
            restTemplate.getForEntity(
                    "http://" + serviceName + "/api/v1/" + serviceName.replace("-service", "s") + "/" + id,
                    Object.class);
            return true;
        } catch (HttpClientErrorException.NotFound exception) {
            return false;
        } catch (RestClientException exception) {
            throw new IllegalStateException("Unable to reach " + serviceName, exception);
        }
    }
}
