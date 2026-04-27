package com.banking.banking_api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;
    @Column(precision = 16, scale = 4, nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name="transaction_type", nullable = false)
    private TransactionType transactionType;

    @Column(name="target_account_number") //nullable for deposits and withdrawals, required for transfers.
    private String targetAccountNumber;
    // The balance AFTER this transaction completed
    // Useful for rendering a bank statement
    @Column(name = "balance_after", precision = 19, scale = 4)
    private BigDecimal balanceAfter;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // The complete set of transaction types our system supports
    // TRANSFER_DEBIT  → money leaving the sender
    // TRANSFER_CREDIT → money arriving at the receiver
    public enum TransactionType {
        DEPOSIT,
        WITHDRAWAL,
        TRANSFER_DEBIT,
        TRANSFER_CREDIT
    }
} 