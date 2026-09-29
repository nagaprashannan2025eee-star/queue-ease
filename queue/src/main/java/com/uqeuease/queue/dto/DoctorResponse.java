package com.uqeuease.queue.dto;

import java.time.LocalDateTime;

public class DoctorResponse {

    private Long id;
    private String name;
    private String specialization;
    private Integer averageConsultationTime;
    private Boolean active;
    private LocalDateTime createdAt;

    public DoctorResponse() {
    }

    public DoctorResponse(
            Long id,
            String name,
            String specialization,
            Integer averageConsultationTime,
            Boolean active,
            LocalDateTime createdAt) {

        this.id = id;
        this.name = name;
        this.specialization = specialization;
        this.averageConsultationTime = averageConsultationTime;
        this.active = active;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
