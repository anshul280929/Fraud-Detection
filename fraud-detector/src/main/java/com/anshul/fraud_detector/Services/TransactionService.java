package com.anshul.fraud_detector.Services;

import com.anshul.fraud_detector.DTO.TransactionRequest;
import com.anshul.fraud_detector.DTO.TransactionResponse;
import com.anshul.fraud_detector.Repositories.TransactionRepository;
import com.anshul.fraud_detector.Repositories.UserRepository;
import com.anshul.fraud_detector.domain.Transaction;
import com.anshul.fraud_detector.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService {
    private final UserRepository userRepo;
    private final TransactionRepository txnRepo;
    private final FraudRuleService rules;

    public TransactionService(UserRepository userRepo, TransactionRepository txnRepo, FraudRuleService rules) {
        this.userRepo = userRepo;
        this.txnRepo = txnRepo;
        this.rules = rules;
    }

    @Transactional
    public TransactionResponse ingest(TransactionRequest req) {
        User user = userRepo.findById(req.userId())
                .orElseGet(() -> userRepo.save(User.builder().userId(req.userId()).city(req.location()).build()));

        FraudRuleService.Decision d = rules.evaluate(user, req.location(), req.deviceId(), req.amount(), req.timestamp());

        Transaction tx = txnRepo.save(Transaction.builder()
                .user(user)
                .amount(req.amount())
                .location(req.location())
                .deviceId(req.deviceId())
                .timestamp(req.timestamp())
                .fraudFlag(d.flag())
                .fraudReason(d.reason())
                .build());

        return new TransactionResponse(tx.getTransactionId(), d.flag(), d.reason(), tx.getFraudScore());
    }

}
