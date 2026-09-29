package com.uqeuease.queue.dto;

import java.time.LocalDateTime;

public class PatientResponse {

    private Long id;
    private String name;
    private String phone;
    private Integer age;
    private String gender;
    private LocalDateTime createdAt;

    public PatientResponse() {
    }

    public PatientResponse(
            Long id,
            String name,
            String phone,
            Integer age,
            String gender,
            LocalDateTime createdAt) {

        this.id = id;
        this.name = name;
        this.phone = phone;
        this.age = age;
        this.gender = gender;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
