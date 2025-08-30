package com.anshul.fraud_detector.DTO;

import jakarta.validation.constraints.*;
import jakarta.validation.constraints.Positive;

import java.time.Instant;

public record TransactionRequest(
        @NotBlank String userId,
        @NotNull @Positive Double amount,
        @NotBlank String location,
        @NotBlank String deviceId,
        @NotNull Instant timestamp
) {}

