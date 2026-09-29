package com.uqeuease.queue.controller;

import com.uqeuease.queue.dto.TokenRequest;
import com.uqeuease.queue.dto.TokenResponse;
import com.uqeuease.queue.entity.Token;
import com.uqeuease.queue.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tokens")
@CrossOrigin(origins = "*")
public class TokenController {

    private final TokenService tokenService;

    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<TokenResponse> generateToken(
            @Valid @RequestBody TokenRequest request) {

        Token token = tokenService.generateToken(
                request.getDoctorId(),
                request.getPatientId(),
                request.getPriority()
        );

        return new ResponseEntity<>(
                toResponse(token),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<TokenResponse>> getTodayQueue(
            @PathVariable Long doctorId) {

        List<TokenResponse> tokens =
                tokenService.getTodayQueue(doctorId)
                        .stream()
                        .map(this::toResponse)
                        .toList();

        return ResponseEntity.ok(tokens);
    }

    @GetMapping("/doctor/{doctorId}/now-serving")
    public ResponseEntity<TokenResponse> getNowServing(
            @PathVariable Long doctorId) {

        Token token = tokenService.getNowServing(doctorId);

        if (token == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(toResponse(token));
    }

    @PutMapping("/doctor/{doctorId}/next")
    public ResponseEntity<TokenResponse> callNext(
            @PathVariable Long doctorId) {

        Token token = tokenService.callNext(doctorId);

        if (token == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(toResponse(token));
    }

    @PutMapping("/{tokenId}/cancel")
    public ResponseEntity<TokenResponse> cancelToken(
            @PathVariable Long tokenId) {

        Token token = tokenService.cancelToken(tokenId);

        return ResponseEntity.ok(toResponse(token));
    }

    private TokenResponse toResponse(Token token) {

        return new TokenResponse(
                token.getId(),
                token.getTokenNumber(),
                token.getTokenDate(),
                token.getPriority(),
                token.getStatus(),
                token.getGeneratedAt(),
                token.getCalledAt(),
                token.getCompletedAt(),
                token.getEstimatedWaitMinutes(),
                token.getDoctor().getId(),
                token.getDoctor().getName(),
                token.getPatient().getId(),
                token.getPatient().getName()
        );
    }
}
