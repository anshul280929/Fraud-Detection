package com.anshul.fraud_detector.Services;

import com.anshul.fraud_detector.Repositories.TransactionRepository;
import com.anshul.fraud_detector.domain.Transaction;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class BatchScoringService {
    private final TransactionRepository repo;

    public BatchScoringService(TransactionRepository repo) { this.repo = repo; }

    // every 5 minutes
    @Scheduled(fixedRate = 300_000)
    public void scoreRecent() {
        Instant tenMinAgo = Instant.now().minusSeconds(600);
        List<Transaction> txns = repo.findByTimestampAfter(tenMinAgo);

        for (Transaction t : txns) {
            double base = Math.tanh(t.getAmount() / 100_000.0); // grows with amount
            double flagBoost = Boolean.TRUE.equals(t.getFraudFlag()) ? 0.3 : 0.0;
            double score = Math.min(1.0, base + flagBoost);
            t.setFraudScore(score);
        }
        repo.saveAll(txns);
    }
}
