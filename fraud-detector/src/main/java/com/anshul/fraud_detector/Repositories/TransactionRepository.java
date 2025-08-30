package com.anshul.fraud_detector.Repositories;


import com.anshul.fraud_detector.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    List<Transaction> findByUser_UserIdOrderByTimestampDesc(String userId);
    List<Transaction> findByTimestampAfter(Instant after);
}
