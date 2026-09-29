
package com.uqeuease.queue.controller;

import com.uqeuease.queue.dto.PatientRequest;
import com.uqeuease.queue.dto.PatientResponse;
import com.uqeuease.queue.entity.Patient;
import com.uqeuease.queue.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "*")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping
    public ResponseEntity<PatientResponse> createPatient(
            @Valid @RequestBody PatientRequest request) {

        Patient patient = Patient.builder()
                .name(request.getName())
                .phone(request.getPhone())
                .age(request.getAge())
                .gender(request.getGender())
                .build();

        Patient savedPatient =
                patientService.createPatient(patient);

        return new ResponseEntity<>(
                toResponse(savedPatient),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<List<PatientResponse>> getAllPatients() {

        List<PatientResponse> patients =
                patientService.getAllPatients()
                        .stream()
                        .map(this::toResponse)
                        .toList();

        return ResponseEntity.ok(patients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientResponse> getPatientById(
            @PathVariable Long id) {

        Patient patient =
                patientService.getPatientById(id);

        return ResponseEntity.ok(
                toResponse(patient)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientResponse> updatePatient(
            @PathVariable Long id,
            @Valid @RequestBody PatientRequest request) {

        Patient updatedPatient = Patient.builder()
                .name(request.getName())
                .phone(request.getPhone())
                .age(request.getAge())
                .gender(request.getGender())
                .build();

        Patient savedPatient =
                patientService.updatePatient(
                        id,
                        updatedPatient
                );

        return ResponseEntity.ok(
                toResponse(savedPatient)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(
            @PathVariable Long id) {

        patientService.deletePatient(id);

        return ResponseEntity.noContent().build();
    }

    private PatientResponse toResponse(Patient patient) {

        return new PatientResponse(
                patient.getId(),
                patient.getName(),
                patient.getPhone(),
                patient.getAge(),
                patient.getGender(),
                patient.getCreatedAt()
        );
    }
}