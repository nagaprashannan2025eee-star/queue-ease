package com.uqeuease.queue.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "tokens",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_doctor_date_token",
            columnNames = {
                "doctor_id",
                "token_date",
                "token_number"
            }
        )
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer tokenNumber;

    @Column(nullable = false)
    private LocalDate tokenDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TokenPriority priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TokenStatus status;

    @Column(nullable = false)
    private LocalDateTime generatedAt;

    private LocalDateTime calledAt;

    private LocalDateTime completedAt;

    private Integer estimatedWaitMinutes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @PrePersist
    protected void onCreate() {

        if (generatedAt == null) {
            generatedAt = LocalDateTime.now();
        }

        if (tokenDate == null) {
            tokenDate = LocalDate.now();
        }

        if (status == null) {
            status = TokenStatus.WAITING;
        }

        if (priority == null) {
            priority = TokenPriority.NORMAL;
        }
    }
}
