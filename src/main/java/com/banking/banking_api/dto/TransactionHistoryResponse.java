package com.banking.banking_api.dto;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import com.banking.banking_api.entity.Transaction;

@Data
@Builder
public class TransactionHistoryResponse {
    private Long id;
    private String type;
    private BigDecimal amount;
    private BigDecimal balanceAfter;
    private String targetAccountNumber;
    private LocalDateTime date;


    public static TransactionHistoryResponse fromEntity(Transaction transaction) {
        return TransactionHistoryResponse.builder()
                .id(transaction.getId())
                .type(transaction.getTransactionType().name())
                .amount(transaction.getAmount())
                .balanceAfter(transaction.getBalanceAfter())
                .targetAccountNumber(transaction.getTargetAccountNumber())
                .date(transaction.getCreatedAt())
                .build();
    }
}