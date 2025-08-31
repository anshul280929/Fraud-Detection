package com.anshul.fraud_detector.Controller;

import com.anshul.fraud_detector.Repositories.TransactionRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController @RequestMapping("api/fraud")
public class FraudController {
    private final TransactionRepository repo;
    public FraudController(TransactionRepository repo) { this.repo = repo; }

    @GetMapping("/summary/{userId}")
    public Map<String, Object> summary(@PathVariable String userId) {
        var list = repo.findByUser_UserIdOrderByTimestampDesc(userId);
        long frauds = list.stream().filter(t -> Boolean.TRUE.equals(t.getFraudFlag())).count();
        return Map.of(
                "userId", userId,
                "total", list.size(),
                "frauds", frauds,
                "lastTxnTs", list.isEmpty() ? null : list.get(0).getTimestamp()
        );
    }
}
