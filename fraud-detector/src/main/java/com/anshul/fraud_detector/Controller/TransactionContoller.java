package com.anshul.fraud_detector.Controller;

import com.anshul.fraud_detector.DTO.TransactionRequest;
import com.anshul.fraud_detector.DTO.TransactionResponse;
import com.anshul.fraud_detector.Repositories.TransactionRepository;
import com.anshul.fraud_detector.Services.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("api/transactions")
public class TransactionContoller {
    private final TransactionService service;
    private final TransactionRepository repo;

    public TransactionContoller(TransactionService service, TransactionRepository repo) {
        this.service = service;
        this.repo = repo;
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> create(@RequestBody @Valid TransactionRequest req) {
        return ResponseEntity.ok(service.ingest(req));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> byUser(@PathVariable String userId) {
        return ResponseEntity.ok(repo.findByUser_UserIdOrderByTimestampDesc(userId));
    }
}
