package com.uqeuease.queue.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class DoctorRequest {

    @NotBlank(message = "Doctor name is required")
    private String name;

    @NotBlank(message = "Specialization is required")
    private String specialization;

    @NotNull(message = "Average consultation time is required")
    @Positive(message = "Average consultation time must be greater than 0")
    private Integer averageConsultationTime;

    private Boolean active = true;

    public DoctorRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public Integer getAverageConsultationTime() {
        return averageConsultationTime;
    }

    public void setAverageConsultationTime(Integer averageConsultationTime) {
        this.averageConsultationTime = averageConsultationTime;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
