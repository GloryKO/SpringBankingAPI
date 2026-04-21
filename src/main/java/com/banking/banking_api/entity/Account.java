package com.banking.banking_api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // One user has exactly one account
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "account_number", nullable = false, unique = true)
    private String accountNumber;

    // Always use BigDecimal for money — never double or float
    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal balance;

    @Column(name = "pin_hash")
    private String pinHash;

    @Column(name = "has_pin", nullable = false)
    private boolean hasPin;

    @Column(name = "ifsc_code")
    private String ifscCode;

    private String branch;

    @Column(name = "account_type")
    private String accountType;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (balance == null) balance = BigDecimal.ZERO;
        if (accountType == null) accountType = "Savings";
        if (ifscCode == null) ifscCode = "NIT001";
        if (branch == null) branch = "NIT";
    }
}