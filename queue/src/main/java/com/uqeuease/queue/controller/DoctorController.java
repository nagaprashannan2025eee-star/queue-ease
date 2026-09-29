package com.uqeuease.queue.controller;

import com.uqeuease.queue.dto.DoctorRequest;
import com.uqeuease.queue.dto.DoctorResponse;
import com.uqeuease.queue.entity.Doctor;
import com.uqeuease.queue.service.DoctorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@CrossOrigin(origins = "*")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping
    public ResponseEntity<DoctorResponse> createDoctor(
            @Valid @RequestBody DoctorRequest request) {

        Doctor doctor = Doctor.builder()
                .name(request.getName())
                .specialization(request.getSpecialization())
                .averageConsultationTime(
                        request.getAverageConsultationTime())
                .active(request.getActive())
                .build();

        Doctor savedDoctor = doctorService.createDoctor(doctor);

        return new ResponseEntity<>(
                toResponse(savedDoctor),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<List<DoctorResponse>> getAllDoctors() {

        List<DoctorResponse> doctors =
                doctorService.getAllDoctors()
                        .stream()
                        .map(this::toResponse)
                        .toList();

        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponse> getDoctorById(
            @PathVariable Long id) {

        Doctor doctor = doctorService.getDoctorById(id);

        return ResponseEntity.ok(toResponse(doctor));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorResponse> updateDoctor(
            @PathVariable Long id,
            @Valid @RequestBody DoctorRequest request) {

        Doctor updatedDoctor = Doctor.builder()
                .name(request.getName())
                .specialization(request.getSpecialization())
                .averageConsultationTime(
                        request.getAverageConsultationTime())
                .active(request.getActive())
                .build();

        Doctor savedDoctor =
                doctorService.updateDoctor(id, updatedDoctor);

        return ResponseEntity.ok(toResponse(savedDoctor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(
            @PathVariable Long id) {

        doctorService.deleteDoctor(id);

        return ResponseEntity.noContent().build();
    }

    private DoctorResponse toResponse(Doctor doctor) {

        return new DoctorResponse(
                doctor.getId(),
                doctor.getName(),
                doctor.getSpecialization(),
                doctor.getAverageConsultationTime(),
                doctor.getActive(),
                doctor.getCreatedAt()
        );
    }
}
