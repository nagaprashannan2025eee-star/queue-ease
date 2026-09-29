package com.uqeuease.queue.repository;

import com.uqeuease.queue.entity.Token;
import com.uqeuease.queue.entity.TokenStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findTopByDoctorIdAndTokenDateOrderByTokenNumberDesc(
            Long doctorId,
            LocalDate tokenDate
    );

    List<Token> findByDoctorIdAndTokenDateOrderByTokenNumberAsc(
            Long doctorId,
            LocalDate tokenDate
    );

    List<Token> findByDoctorIdAndTokenDateAndStatus(
            Long doctorId,
            LocalDate tokenDate,
            TokenStatus status
    );
}
