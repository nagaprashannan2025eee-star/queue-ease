package com.uqeuease.queue.dto;

import com.uqeuease.queue.entity.TokenPriority;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class TokenRequest {

    @NotNull(message = "Doctor ID is required")
    @Positive(message = "Doctor ID must be greater than 0")
    private Long doctorId;

    @NotNull(message = "Patient ID is required")
    @Positive(message = "Patient ID must be greater than 0")
    private Long patientId;

    private TokenPriority priority = TokenPriority.NORMAL;

    public TokenRequest() {
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public TokenPriority getPriority() {
        return priority;
    }

    public void setPriority(TokenPriority priority) {
        this.priority = priority;
    }
}
