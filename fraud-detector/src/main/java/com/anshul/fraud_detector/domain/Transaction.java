package com.anshul.fraud_detector.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity @Table(name="transaction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor @Builder
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name = "user_id")
    private User user;

    private Double amount;
    private String location;       // txn city
    private String deviceId;
    private Instant timestamp;

    private Boolean fraudFlag;
    private String fraudReason;    // short reason
    private Double fraudScore;     // batch ML-like score (0..1)
}
