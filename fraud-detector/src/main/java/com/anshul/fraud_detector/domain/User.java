package com.anshul.fraud_detector.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name= "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    private String userId;
    private String city;

    // 👇 Manual builder method so IntelliJ doesn't complain
    public static UserBuilder builder() {
        return new UserBuilder();
    }
}
