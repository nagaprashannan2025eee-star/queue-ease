package com.uqeuease.queue.service;

import com.uqeuease.queue.entity.Doctor;
import com.uqeuease.queue.entity.Patient;
import com.uqeuease.queue.entity.Token;
import com.uqeuease.queue.entity.TokenPriority;
import com.uqeuease.queue.entity.TokenStatus;
import com.uqeuease.queue.repository.DoctorRepository;
import com.uqeuease.queue.repository.PatientRepository;
import com.uqeuease.queue.repository.TokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class TokenService {

    private final TokenRepository tokenRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    public TokenService(
            TokenRepository tokenRepository,
            DoctorRepository doctorRepository,
            PatientRepository patientRepository) {

        this.tokenRepository = tokenRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    @Transactional
    public Token generateToken(
            Long doctorId,
            Long patientId,
            TokenPriority priority) {

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() ->
                        new RuntimeException("Doctor not found with id: " + doctorId));

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() ->
                        new RuntimeException("Patient not found with id: " + patientId));

        if (!Boolean.TRUE.equals(doctor.getActive())) {
            throw new RuntimeException("Doctor is currently inactive");
        }

        LocalDate today = LocalDate.now();

        if (priority == TokenPriority.EMERGENCY) {

            boolean emergencyExists = tokenRepository
                    .findByDoctorIdAndTokenDateOrderByTokenNumberAsc(
                            doctorId, today)
                    .stream()
                    .anyMatch(token ->
                            token.getPriority() == TokenPriority.EMERGENCY
                                    && (token.getStatus() == TokenStatus.WAITING
                                    || token.getStatus() == TokenStatus.SERVING));

            if (emergencyExists) {
                throw new RuntimeException(
                        "An active priority token already exists for this doctor.");
            }
        }

        int nextTokenNumber = tokenRepository
                .findTopByDoctorIdAndTokenDateOrderByTokenNumberDesc(
                        doctorId, today)
                .map(token -> token.getTokenNumber() + 1)
                .orElse(1);

        Token token = Token.builder()
                .tokenNumber(nextTokenNumber)
                .tokenDate(today)
                .priority(priority == null
                        ? TokenPriority.NORMAL
                        : priority)
                .status(TokenStatus.WAITING)
                .generatedAt(LocalDateTime.now())
                .estimatedWaitMinutes(0)
                .doctor(doctor)
                .patient(patient)
                .build();

        Token savedToken = tokenRepository.save(token);

        updateWaitingTimes(doctorId, today);

        return savedToken;
    }

    public List<Token> getTodayQueue(Long doctorId) {

        LocalDate today = LocalDate.now();

        return tokenRepository
                .findByDoctorIdAndTokenDateOrderByTokenNumberAsc(
                        doctorId, today);
    }

    public Token getNowServing(Long doctorId) {

        LocalDate today = LocalDate.now();

        return tokenRepository
                .findByDoctorIdAndTokenDateAndStatus(
                        doctorId,
                        today,
                        TokenStatus.SERVING)
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Transactional
    public Token callNext(Long doctorId) {

        LocalDate today = LocalDate.now();

        List<Token> servingTokens =
                tokenRepository.findByDoctorIdAndTokenDateAndStatus(
                        doctorId,
                        today,
                        TokenStatus.SERVING);

        if (!servingTokens.isEmpty()) {

            Token currentToken = servingTokens.get(0);

            currentToken.setStatus(TokenStatus.COMPLETED);
            currentToken.setCompletedAt(LocalDateTime.now());

            tokenRepository.save(currentToken);
        }

        List<Token> waitingTokens =
                tokenRepository.findByDoctorIdAndTokenDateAndStatus(
                        doctorId,
                        today,
                        TokenStatus.WAITING);

        if (waitingTokens.isEmpty()) {
            return null;
        }

        Token nextToken = waitingTokens.stream()
                .sorted(
                        Comparator
                                .comparing(
                                        (Token token) ->
                                                token.getPriority()
                                                        == TokenPriority.EMERGENCY)
                                .reversed()
                                .thenComparing(Token::getTokenNumber)
                )
                .findFirst()
                .orElse(null);

        if (nextToken == null) {
            return null;
        }

        nextToken.setStatus(TokenStatus.SERVING);
        nextToken.setCalledAt(LocalDateTime.now());
        nextToken.setEstimatedWaitMinutes(0);

        Token savedToken = tokenRepository.save(nextToken);

        updateWaitingTimes(doctorId, today);

        return savedToken;
    }

    @Transactional
    public Token cancelToken(Long tokenId) {

        Token token = tokenRepository.findById(tokenId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Token not found with id: " + tokenId));

        if (token.getStatus() != TokenStatus.WAITING) {
            throw new RuntimeException(
                    "Only WAITING tokens can be cancelled.");
        }

        token.setStatus(TokenStatus.CANCELLED);

        Token cancelledToken = tokenRepository.save(token);

        updateWaitingTimes(
                token.getDoctor().getId(),
                token.getTokenDate());

        return cancelledToken;
    }

    private void updateWaitingTimes(
            Long doctorId,
            LocalDate date) {

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Doctor not found with id: " + doctorId));

        int consultationTime =
                doctor.getAverageConsultationTime();

        List<Token> waitingTokens =
                tokenRepository.findByDoctorIdAndTokenDateAndStatus(
                        doctorId,
                        date,
                        TokenStatus.WAITING);

        List<Token> servingTokens =
                tokenRepository.findByDoctorIdAndTokenDateAndStatus(
                        doctorId,
                        date,
                        TokenStatus.SERVING);

        int currentServingCount = servingTokens.isEmpty() ? 0 : 1;

        waitingTokens.sort(
                Comparator
                        .comparing(
                                (Token token) ->
                                        token.getPriority()
                                                == TokenPriority.EMERGENCY)
                        .reversed()
                        .thenComparing(Token::getTokenNumber)
        );

        for (int i = 0; i < waitingTokens.size(); i++) {

            Token token = waitingTokens.get(i);

            int tokensAhead =
                    currentServingCount + i;

            token.setEstimatedWaitMinutes(
                    tokensAhead * consultationTime
            );
        }

        tokenRepository.saveAll(waitingTokens);
    }
}
