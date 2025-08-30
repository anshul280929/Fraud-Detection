package com.anshul.fraud_detector.DTO;

public record TransactionResponse(
        Long transactionId,
        boolean fraudFlag,
        String reason,
        Double score
) {
}
