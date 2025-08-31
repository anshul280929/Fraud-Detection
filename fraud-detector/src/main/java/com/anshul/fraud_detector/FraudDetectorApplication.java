package com.anshul.fraud_detector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {
		org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration.class
})
@EnableScheduling
public class FraudDetectorApplication {

	public static void main(String[] args) {
		SpringApplication.run(FraudDetectorApplication.class, args);
	}

}
