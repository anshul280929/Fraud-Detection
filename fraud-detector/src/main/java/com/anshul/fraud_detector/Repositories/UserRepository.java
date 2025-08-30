package com.anshul.fraud_detector.Repositories;

import com.anshul.fraud_detector.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,String> {}
