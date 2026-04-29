package com.banking.banking_api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "otp_records")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OtpRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The email this OTP was sent to
    @Column(nullable = false)
    private String email;

    // The 6 digit OTP code
    @Column(nullable = false)
    private String otp;

    // When this OTP stops being valid
    // We set this to 5 minutes from creation time
    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}