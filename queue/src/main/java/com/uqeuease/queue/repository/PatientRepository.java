package com.uqeuease.queue.repository;

import com.uqeuease.queue.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}
