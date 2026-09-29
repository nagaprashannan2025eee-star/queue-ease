package com.uqeuease.queue.dto;

import com.uqeuease.queue.entity.TokenPriority;
import com.uqeuease.queue.entity.TokenStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TokenResponse {

    private Long id;
    private Integer tokenNumber;
    private LocalDate tokenDate;
    private TokenPriority priority;
    private TokenStatus status;
    private LocalDateTime generatedAt;
    private LocalDateTime calledAt;
    private LocalDateTime completedAt;
    private Integer estimatedWaitMinutes;

    private Long doctorId;
    private String doctorName;

    private Long patientId;
    private String patientName;

    public TokenResponse() {
    }

    public TokenResponse(
            Long id,
            Integer tokenNumber,
            LocalDate tokenDate,
            TokenPriority priority,
            TokenStatus status,
            LocalDateTime generatedAt,
            LocalDateTime calledAt,
            LocalDateTime completedAt,
            Integer estimatedWaitMinutes,
            Long doctorId,
            String doctorName,
            Long patientId,
            String patientName) {

        this.id = id;
        this.tokenNumber = tokenNumber;
        this.tokenDate = tokenDate;
        this.priority = priority;
        this.status = status;
        this.generatedAt = generatedAt;
        this.calledAt = calledAt;
        this.completedAt = completedAt;
        this.estimatedWaitMinutes = estimatedWaitMinutes;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.patientId = patientId;
        this.patientName = patientName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTokenNumber() {
        return tokenNumber;
    }

    public void setTokenNumber(Integer tokenNumber) {
        this.tokenNumber = tokenNumber;
    }

    public LocalDate getTokenDate() {
        return tokenDate;
    }

    public void setTokenDate(LocalDate tokenDate) {
        this.tokenDate = tokenDate;
    }

    public TokenPriority getPriority() {
        return priority;
    }

    public void setPriority(TokenPriority priority) {
        this.priority = priority;
    }

    public TokenStatus getStatus() {
        return status;
    }

    public void setStatus(TokenStatus status) {
        this.status = status;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public LocalDateTime getCalledAt() {
        return calledAt;
    }

    public void setCalledAt(LocalDateTime calledAt) {
        this.calledAt = calledAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public Integer getEstimatedWaitMinutes() {
        return estimatedWaitMinutes;
    }

    public void setEstimatedWaitMinutes(Integer estimatedWaitMinutes) {
        this.estimatedWaitMinutes = estimatedWaitMinutes;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }
}
