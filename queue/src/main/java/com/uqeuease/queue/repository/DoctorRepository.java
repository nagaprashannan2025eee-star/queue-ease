package com.uqeuease.queue.repository;

import com.uqeuease.queue.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}
