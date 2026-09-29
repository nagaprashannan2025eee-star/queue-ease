package com.uqeuease.queue.service;

import com.uqeuease.queue.entity.Doctor;
import com.uqeuease.queue.repository.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public Doctor createDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Doctor not found with id: " + id));
    }

    public Doctor updateDoctor(Long id, Doctor updatedDoctor) {

        Doctor existingDoctor = getDoctorById(id);

        existingDoctor.setName(updatedDoctor.getName());
        existingDoctor.setSpecialization(
                updatedDoctor.getSpecialization()
        );
        existingDoctor.setAverageConsultationTime(
                updatedDoctor.getAverageConsultationTime()
        );
        existingDoctor.setActive(updatedDoctor.getActive());

        return doctorRepository.save(existingDoctor);
    }

    public void deleteDoctor(Long id) {
        Doctor doctor = getDoctorById(id);
        doctorRepository.delete(doctor);
    }
}
